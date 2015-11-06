package com.example.saikrishna.raven.Extras;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sai Krishna on 04-07-2015.
 */
public class Information {
    public class Message{
        String message;
        boolean flag;
        public Message(){this.message="";this.flag=false;}
        public Message(String msg,boolean falg){this.message=msg;this.flag=falg;}
        public String getMessage(){return this.message;}
        public boolean getFlag(){return this.flag;}
    }
    public Message createMessage(String message,  boolean flag) { Message message1=new Message(); message1.message=message; message1.flag=flag; return message1;}

    public class Contact_Info {
        String name;
        int number;
        public String getName(){return this.name;}
        public int getNumber(){return this.number;}
    }
    private Contact_Info createContact_Info(String name,  int number) { Contact_Info contact_info=new Contact_Info();contact_info.name=name; contact_info.number=number;return contact_info;}

    public class Block_Contact implements Serializable{
        List<Message> messages = new ArrayList<Message>();
        Contact_Info contact;
        public List<Message>  getMessageList(){return this.messages;}
        public Contact_Info getContact(){return this.contact;}
        public void addMessage(String message1,boolean flag)
        {Message msg=new Message();
            msg=createMessage(message1,flag);
            this.messages.add(msg);
            return;
        }
        public int getMessagesSize(){return this.messages.size();}
    }
    private Block_Contact createBlock_Contact(String name,int number) {
        Block_Contact one_person=new Block_Contact();

        one_person.messages.add(new Message("Hi", false));
        one_person.messages.add(new Message("Hello", true));
        one_person.contact=createContact_Info(name,number);
        return one_person;
    }

    List<Block_Contact> info;
    public List<Block_Contact> getInformation(){return this.info;}
    public int getInformationSize(){return this.info.size();}
    public Block_Contact getItem(int position){return this.info.get(position);}

    public Information(){
        info=new ArrayList<Block_Contact>();
        String name;
        int number;
        String message;
        boolean flag;
        name="Sai";
        number=123;
        info.add(createInformation(name, number));

        name="Sandesh";
        number=123;
        info.add(createInformation(name, number));

        name="Pavan";
        number=123;
        info.add(createInformation(name, number));

        name="Ajith";
        number=123;
        info.add(createInformation(name, number));

        name="Raul";
        number=123;
        info.add(createInformation(name, number));

        name="Vemosh";
        number=123;
        info.add(createInformation(name, number));

        name="Sai";
        number=123;
        info.add(createInformation(name, number));

        name="Sandesh";
        number=123;
        info.add(createInformation(name, number));

        name="Pavan";
        number=123;
        info.add(createInformation(name, number));

        name="Ajith";
        number=123;
        info.add(createInformation(name, number));

        name="Raul";
        number=123;
        info.add(createInformation(name, number));

        name="Vemosh";
        number=123;
        info.add(createInformation(name, number));

        name="Sai";
        number=123;
        info.add(createInformation(name, number));

        name="Sandesh";
        number=123;
        info.add(createInformation(name, number));

        name="Pavan";
        number=123;
        info.add(createInformation(name, number));

        name="Ajith";
        number=123;
        info.add(createInformation(name, number));

        name="Raul";
        number=123;
        info.add(createInformation(name, number));

        name="Vemosh";
        number=123;
        info.add(createInformation(name, number));
    }
    public Block_Contact createInformation(String name,int number){Block_Contact block_contact=new Block_Contact();
        block_contact=createBlock_Contact(name,number);
        return block_contact;
    }
}

