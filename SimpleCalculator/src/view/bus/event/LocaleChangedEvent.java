package view.bus.event;

import java.util.Locale;

public class LocaleChangedEvent implements SapperViewEvent<Locale> {
    private final Locale locale;

    public LocaleChangedEvent(Locale locale) {
        this.locale = locale;
    }

    @Override
    public Locale getData() {
        return locale;
    }
}
