package com.taoy3.framwork.ActivityService;

import android.app.Service;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.taoy3.framwork.R;

public class AudioActivity extends AppCompatActivity {

    private AudioManager aManager;
    private Button play;
    private Button up;
    private Button down;
    private ToggleButton mute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        //获取系统音频服务
        aManager = (AudioManager)getSystemService(Service.AUDIO_SERVICE);
        play= (Button)findViewById(R.id.play);
        up = (Button)findViewById(R.id.up);
        down = (Button)findViewById(R.id.down);
        mute = (ToggleButton)findViewById(R.id.toggle);

        play.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
//                初始化MediaPlayer对象，准备播放音乐，
//                其中的R.raw.earth引用的是res资源文件中的一个MP3音频文件，需要在res文件中新建一个名为raw的
//                文件，在其中放入一个名为earth.mp3的文件即可
                MediaPlayer mPlayer = MediaPlayer.create(AudioActivity.this, R.raw.alarm);
                mPlayer.setLooping(true) ;
                mPlayer.start();

            }
        });

        up.setOnClickListener(new View.OnClickListener(){
//            ①adjustStreamVolume（int streamType ， int direction , int flags）
//            调整手机指定类型的声音。其中第一个参数streamType是定声音的类型，该参数可接受如下的几个值：
//            STREAM_ALARM：手机闹铃的声音
//            STREAM_DTMF：DTMF音调声音
//            STREAM_MUSIC:手机音乐的声音
//            STREAM_NOTIFICATION：系统提示音
//            STREAM_SYSTEM：手机系统的声音
//            STREAM_RING：手机铃声的声音
//            STREAM_VOICE_CALL：语音电话的声音
//            第二个参数指定对声音的增大和减少，
//            ADJUST_LOWER
//            ADJUST_RAISE 加大
//            ADJUST_SAME 正常
//            第三个参数是调整声音时显示的标志，例如指定FLAG_SHPW_UI，
//            则在调整声音的大小的时候将会出现一个显示几秒钟的音量圈
//            ②setMicrophoneMute（boolean on ）设置是否让麦克静音
//            ③setMode（int mode ）设置声音的模式，可设置值有NORMAL ，RINGTONE、IN_CALL
//            ④setRingerMode（int ringerMode） 设置手机的电话铃声模式，可支持一下几种属性值：
//            RINGER_MODE_NORMAL：正常的手机铃声
//            RINGER_MODE_SILENT：手机铃声静音
//            RINGER_MODE_VIBRATE：手机震动
//            ⑤setSpeakerphoneOn（boolean on）：设置是否打开扩音器
//            ⑥setStreamMute（int streamType , boolean state）
//            将手机的指定类型的声音调整为静音，其中的streamType参数与adjustStreamVolume方法中的第一个参数的意义相同。
//            ⑦setStreamVolume（int streamType ， int index ，int flags）
//            直接设置手机的指定类型的音量值。其中第一个参数的意义和上面的那个参数一样
//            下面就举一个例子，这个例子用来播放音乐，布局中有四个按钮，play用于播放音乐 ， up用于增大音量 ， down用于减少音量 ， 还有一个ToggleButton 开关 mute用于指定是否静音，其中使用了MediaPlayer来播放音乐，
            @Override
            public void onClick(View v) {
                aManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
            }
        });

        down.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                aManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
            }
        });

        mute.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                //指定是否需要静音
                aManager.setStreamMute(AudioManager.STREAM_MUSIC, isChecked);
            }
        });
    }
    }

