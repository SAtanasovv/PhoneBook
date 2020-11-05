package com.satanasov.phonebook.presenter;

import com.satanasov.phonebook.model.ContactModel;

import java.util.ArrayList;

public interface MainActivityView{

void setContactListInRecyclerView(ArrayList<ContactModel> contactModelList);

}
