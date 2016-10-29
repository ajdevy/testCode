package livecoding.ask.fm.askfmlivecoding.inject;

import javax.inject.Singleton;

import dagger.Component;
import livecoding.ask.fm.askfmlivecoding.MainActivity;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(MainActivity activity);
    // void inject(MyFragment fragment);
    // void inject(MyService service);
}