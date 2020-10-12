package com.example.daggerexample;

import javax.inject.Singleton;

import dagger.Component;
import com.example.daggerexample.MainActivity;

@Singleton
@Component(modules = {SharedPrefModule.class})
public interface MyComponent {
    void inject(MainActivity activity);
}
