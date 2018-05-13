package reshrink.zcw.com.study;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import reshrink.zcw.com.study.bean.MessageEvent;
import reshrink.zcw.com.study.eventbus.EventBus;
import reshrink.zcw.com.study.eventbus.Subscribe;
import reshrink.zcw.com.study.eventbus.ThreadMode;
import reshrink.zcw.com.study.utils.LogUtils;

public class BActivity extends AppCompatActivity {

     public static void start(Context context) {
         Intent starter = new Intent(context, BActivity.class);

         context.startActivity(starter);
     }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
        EventBus.getDefault().register(this);
    }

    public void sendEvent(View view){
        MessageEvent messageEvent=new MessageEvent();
        messageEvent.setMsgId(1000);
        messageEvent.setMsgContent("发送消息：bbbbbbbbbbbb");
        EventBus.getDefault().post(messageEvent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MessageEvent event) {

        LogUtils.printLogSD(LogUtils.ERROR,"BActivity:"+event.toString());
    }

}
