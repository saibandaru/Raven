package layout.chat;

/**
 * Created by Sai Krishna on 02-07-2015.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MsgData {

    List<Map<String, ?>> msgInfo;

    public List<Map<String, ?>> getMsgInfo() {
        return msgInfo;
    }

    public int getSize() {
        return msgInfo.size();
    }

    public HashMap getItem(int i) {
        if (i >= 0 && i < msgInfo.size()) {
            return (HashMap) msgInfo.get(i);
        } else return null;
    }

    public MsgData() {
        String name;
        String msg;
        boolean flag;
        msgInfo = new ArrayList<Map<String, ?>>();
        //#1-10
        name = "KD";
        msg = "Hi!!";
        flag = false;
        msgInfo.add(createMsg(name, msg, flag));
        msg = "Hello!!";
        flag = true;
        msgInfo.add(createMsg(name, msg, flag));
        msg = "How r ya?";
        flag = false;
        msgInfo.add(createMsg(name, msg, flag));
        msg = "I'm good..How abut u?";
        flag = true;
        msgInfo.add(createMsg(name, msg, flag));
        msg = "T am good too";
        flag = false;
        msgInfo.add(createMsg(name, msg, flag));


        msg = "Hi!!";
        flag = false;
        msgInfo.add(createMsg(name, msg, flag));
        msg = "Hello!!";
        flag = true;
        msgInfo.add(createMsg(name, msg, flag));
        msg = "How r ya?";
        flag = false;
        msgInfo.add(createMsg(name, msg, flag));
        msg = "I'm good..How abut u?";
        flag = true;
        msgInfo.add(createMsg(name, msg, flag));
        msg = "T am good too";
        flag = false;
        msgInfo.add(createMsg(name, msg, flag));
        msg = "Hi!!";
        flag = false;
        msgInfo.add(createMsg(name, msg, flag));
        msg = "Hello!!";
        flag = true;
        msgInfo.add(createMsg(name, msg, flag));
        msg = "How r ya?";
        flag = false;
        msgInfo.add(createMsg(name, msg, flag));
        msg = "I'm good..How abut u?";
        flag = true;
        msgInfo.add(createMsg(name, msg, flag));
        msg = "T am good too";
        flag = false;
        msgInfo.add(createMsg(name, msg, flag));
        msg = "Hi!!";
        flag = false;
        msgInfo.add(createMsg(name, msg, flag));
        msg = "Hello!!";
        flag = true;
        msgInfo.add(createMsg(name, msg, flag));
        msg = "How r ya?";
        flag = false;
        msgInfo.add(createMsg(name, msg, flag));
        msg = "I'm good..How abut u?";
        flag = true;
        msgInfo.add(createMsg(name, msg, flag));
        msg = "T am good too ...hey do u rembember that incident, where we skipped the train and went on a freey";
        flag = false;
        msgInfo.add(createMsg(name, msg, flag));
    }

    private HashMap createMsg(String name, String msg, boolean flag) {
        HashMap message = new HashMap();
        message.put("name", name);
        message.put("msg", msg);
        message.put("in_out", flag);
        return message;

    }
}