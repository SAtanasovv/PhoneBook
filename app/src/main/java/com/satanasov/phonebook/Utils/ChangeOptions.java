package com.satanasov.phonebook.Utils;

public enum ChangeOptions {
    ADD_CONTACT(1) ,
    EDIT_CONTACT(2),
    VIEW_CONTACT(3);

   private int choseOption;
   private ChangeOptions(int choseOption){
       this.choseOption = choseOption;

   }
   public int getOption(){
       return choseOption;
   }

}
