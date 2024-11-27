package com.example.resourceproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //Метод attachBaseContext() спрацьовує при завантаженні актівіті до методу onCreate().
    //цей метод також бере участь в життевому циклi актiвiтi , i вiн запускаэться ранiше нiж onCreate
    @Override
    protected void attachBaseContext(Context contextBase) {//в этом методе еще нет активити, але ми можем через  SharedPreference достать настройки
        //Приклад отримання налаштувань з SharedPreferences
        SharedPreferences preferences =
                contextBase.getSharedPreferences("settings", MODE_PRIVATE);//getSharedPreferences вызывается от contextBase , так как активити еще нет
        String localeTag =
                preferences.getString("locale", Locale.ENGLISH.toLanguageTag());
        //Новий спосіб встановлення локалізації
        Locale locale = Locale.forLanguageTag(localeTag);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        Context newContext =
                contextBase.createConfigurationContext(configuration);

        super.attachBaseContext(newContext);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        String localeTag = preferences.getString("locale", Locale.ENGLISH.toLanguageTag());//если ранее сохраненной локализации не было, то устанавливаем англ по умолчанию
        setLocale(Locale.forLanguageTag(localeTag));

        //Встановлення локалізованого тексту із ресурсів у заголовок актівіті
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }

        //Theme
        //setTheme(R.style.Base_Theme_ResourceProject);
        //setTheme(R.style.Blue_Theme_ResourceProject);
        int themeId = preferences.getInt("theme_id", R.style.Blue_Theme_ResourceProject);  // default
        setTheme(themeId);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Log.d("t", "onCreate: ");
        //Locale.   и можем увидеть все константы для локализации
        //Locale.getDefault();

    }

    //створення Option Menu,вложенное меню
    //! также, нужно в themes.xml удалить NoActionBar !
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);//объединяем разметку и обьект меню
        return super.onCreateOptionsMenu(menu);
    }

    //обробка натискання на пункти меню
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//в параметр приходит объект айтема, на который кликнули
        int itemId = item.getItemId();

        //сохранение настроек
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();//метод edit() дает нам обьект Editor'а который может работать с настройкоми

        //Orientations

        if (itemId == R.id.portraitMenu) {
            Toast.makeText(this, R.string.portrait, Toast.LENGTH_SHORT).show();//вспливаючi повiдомлення,для iнформування. в параметре контекст- это активити, мы находимся в активити, поэтому передаем this. а второй парамерт R.string.portrait -это вместо того, чтоб хардкодить : text"Portrait'

            //тут мы можем проверить какая включена ориентация, и включить ее
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }
        else if (itemId == R.id.landscapeMenu) {
            //а в этом варианте получим объект Toast
            Toast toast = Toast.makeText(this, R.string.landscape, Toast.LENGTH_LONG);
            //addCallback для реакции показу дiалогу i закриття
            toast.addCallback(new Toast.Callback() { //принимает колбэк -обработку события, создание анонимного класса, на основе класса колбэка
                @Override//переопределяем родительский метод класса Callback
                public void onToastShown() {
                    super.onToastShown();
                    Log.d("t", "onToastShown: ");
                }

                @Override
                public void onToastHidden() {
                    super.onToastHidden();
                    Log.d("t", "onToastHidden: ");
                }
            });
            toast.show();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            //ActivityInfo.SCREEN_ORIENTATION_LOCKED

        } else if (itemId == R.id.sensorMenu) {
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_SENSOR) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                Toast.makeText(this, R.string.sensor, Toast.LENGTH_SHORT).show();
            }
        } else if (itemId == R.id.reverseLandscapeMenu) {
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                Toast.makeText(this, R.string.reverse_landscape, Toast.LENGTH_SHORT).show();
            }
        } else if (itemId == R.id.reversePortraitMenu) {
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                Toast.makeText(this, R.string.reverse_portrait, Toast.LENGTH_SHORT).show();
            }
        } else if (itemId == R.id.userPortraitMenu) {
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
                Toast.makeText(this, R.string.user_portrait, Toast.LENGTH_SHORT).show();
            }
        }else if (itemId == R.id.unspecifiedMenu) {
            Toast.makeText(this, R.string.unspecified, Toast.LENGTH_SHORT).show();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }else if (itemId == R.id.sensorPortraitMenu) {
            Toast.makeText(this, R.string.sensor_portrait, Toast.LENGTH_SHORT).show();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        } else if (itemId == R.id.sensorLandscapeMenu) {
            Toast.makeText(this, R.string.sensor_landscape, Toast.LENGTH_SHORT).show();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else if (itemId == R.id.fullSensorMenu) {
            Toast.makeText(this, R.string.full_sensor, Toast.LENGTH_SHORT).show();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        } else if (itemId == R.id.fullUserMenu) {
            Toast.makeText(this, R.string.full_user, Toast.LENGTH_SHORT).show();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_USER);
        } else if (itemId == R.id.lockedMenu) {
            Toast.makeText(this, R.string.locked, Toast.LENGTH_SHORT).show();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

//Locale
        } else if (itemId == R.id.enLocaleMenu) {
            //добавляем в эдитор(инструмент, который редактирует настройми SharedPreferences)  ключ и значение временно, а потом уже в SharedPreferences
            //в свою очередь SharedPreferences сохраняет настройки в InternalStorage(в папку программы в файловой системе)
            editor.putString("locale", Locale.ENGLISH.toLanguageTag());//зберiгаемо налаштування
            editor.apply();
            recreate();
            // setLocale(Locale.ENGLISH);//("en");

        } else if (itemId == R.id.enGBLocaleMenu) {//а если сюда кликаем, то по ключу "locale" запишется GB
            editor.putString("locale", Locale.UK.toLanguageTag());
            editor.apply();
            recreate();
            //setLocale(Locale.UK); //GB

        } else if (itemId == R.id.ukLocaleMenu) {
            editor.putString("locale", Locale.forLanguageTag("uk").toLanguageTag());
            editor.apply();
            recreate();
            //  setLocale(new Locale("uk"));

        }

//Themes
        else if (itemId == R.id.lightThemeOption) {
            editor.putInt("theme_id", R.style.Light_Theme_MyProject).apply();
            recreate();
        } else if (itemId == R.id.darkThemeOption) {
            editor.putInt("theme_id", R.style.Dark_Theme_MyProject).apply();
            recreate();
        } else if (itemId == R.id.blueThemeOption) {
            editor.putInt("theme_id", R.style.Blue_Theme_MyProject).apply();
            recreate();
        }
        else if (itemId == R.id.colorThemeOption) {
            editor.putInt("theme_id", R.style.Color_Theme_MyProject).apply();
            recreate();
        }

        return super.onOptionsItemSelected(item);

    }

    private void setLocale(Locale locale) { //(string languageCode){
        // Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = getResources();//это метод от активити, который достает объект, через который мы можем управлять ресурсом( в данном случае локализацией)
        Configuration configuration = resources.getConfiguration();//достаем конфигурацию/конфiгурацiя це налаштування якi доступнi поки у нас программа запущена, а коли ми программу закриваемо, то i налаштування зтираються
        configuration.setLocale(locale);//в конфигурацию устанавливаем локализацию
        // resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        getBaseContext().getResources().updateConfiguration(
                configuration, resources.getDisplayMetrics()
        );

        // recreate();//функция активити, которая пересоздает активити т.е снова срабатывает onCreate
        //2 вариант  recreate();
        //       Intent intent = new Intent(this, MainActivity.class);
        //       startActivity(intent);
        //       finish();
    }


}