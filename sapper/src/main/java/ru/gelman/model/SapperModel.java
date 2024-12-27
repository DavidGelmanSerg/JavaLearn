package ru.gelman.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gelman.common.SapperDifficulty;
import ru.gelman.common.Time;
import ru.gelman.common.dto.CellData;
import ru.gelman.common.observer.SapperListener;
import ru.gelman.common.observer.SapperObserver;
import ru.gelman.common.observer.events.SapperEvent;
import ru.gelman.common.observer.events.SapperEventFactory;
import ru.gelman.common.observer.events.SapperEventType;
import ru.gelman.model.field.SapperField;
import ru.gelman.model.field.SapperFieldFactory;
import ru.gelman.model.repository.records.RecordsRepository;
import ru.gelman.model.repository.records.h2.H2Repository;
import ru.gelman.model.repository.records.h2.PlayerRecord;
import ru.gelman.timer.TimerListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SapperModel implements SapperObserver, TimerListener {
    private static final Time deadline = new Time(59, 59);
    private static final Time ZERO_TIME = new Time(0, 0);
    private static final int RECORDS_LIMIT = 10;
    private final List<SapperListener> listeners;
    private final SapperEventFactory eventFactory;
    private final RecordsRepository recordsRepository;
    private SapperDifficulty difficulty;
    private SapperGameStatus status;
    private SapperField field;
    private Time currentGameTime;
    private final Logger logger = LoggerFactory.getLogger(SapperModel.class);

    public SapperModel() {
        listeners = new ArrayList<>();
        eventFactory = new SapperEventFactory();
        difficulty = SapperDifficulty.BEGINNER;
        field = SapperFieldFactory.getField(difficulty);
        recordsRepository = new H2Repository();
        setStatus(SapperGameStatus.NOT_STARTED);
        currentGameTime = ZERO_TIME;
    }


    public void start() {
        logger.info("Starting new game with difficulty: {}", difficulty);
        setStatus(SapperGameStatus.INIT);
        currentGameTime = ZERO_TIME;
        notifyListeners(eventFactory.getInitEvent(field.getSide(), field.getFlags()));
    }

    public void start(SapperDifficulty difficulty) {
        logger.info("Starting new game with difficulty: {}", difficulty);
        currentGameTime = ZERO_TIME;
        this.difficulty = difficulty;
        field = SapperFieldFactory.getField(difficulty);
        start();
    }

    public void restart() {
        logger.info("Restarting game");
        currentGameTime = ZERO_TIME;
        field = SapperFieldFactory.getField(difficulty);
        start();
    }

    public void openCell(int x, int y) {
        if (!inProcess()) {
            logger.info("FIRST CLICK. SETTING BOMBS");
            field.setBombs(x, y);
            setStatus(SapperGameStatus.IN_PROCESS);
            notifyListeners(eventFactory.getStartEvent());
            logger.info("GAME STARTED");
        }

        if (field.isBomb(x, y) && !field.isMarked(x, y)) {
            logger.info("GAME FINISHED: LOOSE");
            setStatus(SapperGameStatus.LOOSE);
            notifyListeners(eventFactory.getCellsChangedEvent(field.getCellsWhenLoose(x, y)));
            notifyListeners(eventFactory.getLooseEvent());
            return;
        }

        logger.info("Opening Cell[{},{}]",x,y);
        List<CellData> openedCells = field.open(x, y);
        if (openedCells != null) {
            notifyListeners(eventFactory.getCellsChangedEvent(openedCells));
            if (field.isWin()) {
                logger.info("GAME FINISHED: WIN");
                setStatus(SapperGameStatus.WIN);
                notifyListeners(eventFactory.getWinEvent(isRecord()));
            }
        }
    }

    private boolean isRecord() {
        List<PlayerRecord> records = recordsRepository.getRecords(difficulty);
        Collections.sort(records);
        if(!records.isEmpty()){
            logger.info("HAS RECORDS. CHECKING THE RECORD");
            Time lastRecordPoints = records.get(records.size() - 1).getPoints();
            logger.info("RESULT: {}",records.size() < RECORDS_LIMIT || currentGameTime.compareTo(lastRecordPoints) < 0);
            return records.size() < RECORDS_LIMIT || currentGameTime.compareTo(lastRecordPoints) < 0;
        }
        logger.info("NO RECORDS.RETURN TRUE");
        return true;
    }

    public void markCell(int x, int y) {
        logger.info("Marking Cell[{},{}]",x,y);
        field.mark(x, y);
        List<CellData> cellData = List.of(field.getCellData(x, y));
        int flags = field.getFlags();
        logger.info("Completed successful");

        notifyListeners(eventFactory.getCellsChangedEvent(cellData));
        notifyListeners(eventFactory.getFlagsChangedEvent(flags));
    }

    public void getPlayersRecords() {
        List<PlayerRecord> records = recordsRepository.getRecords(difficulty);
        Collections.sort(records);
        SapperEvent recordsEvent = eventFactory.getSelectedPlayersEvent(PlayerRecord.getPlayerRecordsData(records));
        notifyListeners(recordsEvent);
    }

    public void saveRecord(String playerName) {
        if(playerName == null){
            notifyListeners(eventFactory.getSaveRecordEvent(false));
            return;
        }
        PlayerRecord record = recordsRepository.getRecord(playerName, difficulty);
        boolean isComplete;
        if (record != null && record.getPoints().compareTo(currentGameTime) < 0) {
            record.setTime(currentGameTime);
            isComplete = recordsRepository.update(record);
        } else {
            isComplete = recordsRepository.add(new PlayerRecord(playerName, difficulty, currentGameTime));
        }
        notifyListeners(eventFactory.getSaveRecordEvent(isComplete));
    }

    private void setStatus(SapperGameStatus status) {
        logger.debug("Game status set to {}",status);
        this.status = status;
    }

    private boolean inProcess() {
        return status == SapperGameStatus.IN_PROCESS;
    }

    @Override
    public void addSapperListener(SapperListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeSapperListener(SapperListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void clearListenerList() {
        listeners.clear();
    }

    @Override
    public void notifyListeners(SapperEvent event) {
        for (SapperListener l : listeners) {
            if(event.getType() != SapperEventType.TIME_CHANGED){
                logger.info("sending {} event to {}",event.getType(),l);
                logger.debug("event: {}",event);
            }
            l.update(event);
        }
    }

    @Override
    public void onTimerTick(Time time) {
        currentGameTime = Time.of(time);
        if (status == SapperGameStatus.IN_PROCESS) {
            if (currentGameTime.compareTo(deadline) < 0) {
                notifyListeners(eventFactory.getTimeChangedEvent(currentGameTime));
            } else {
                notifyListeners(eventFactory.getLooseEvent());
            }
        }
    }
}
