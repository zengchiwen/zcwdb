package reshrink.zcw.com.study.bean;

/**
 * Created by lenovo on 2018/1/30.
 */

public class MessageEvent {

    private  int msgId;
    private  int msgValue;
    private String msgContent;

    public String getMsgLas() {
        return msgLas;
    }

    public void setMsgLas(String msgLas) {
        this.msgLas = msgLas;
    }

    private String msgLas;

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public int getMsgValue() {
        return msgValue;
    }

    public void setMsgValue(int msgValue) {
        this.msgValue = msgValue;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    @Override
    public String toString() {
        return "MessageEvent{" +
                "msgId=" + msgId +
                ", msgValue=" + msgValue +
                ", msgContent='" + msgContent + '\'' +
                ", msgLas='" + msgLas + '\'' +
                '}';
    }
}
