package storage;

//TODO: consider making this an interface

/*******************************************
 * 				Account					   *
 * This abstract class has the main aim to * 
 * enable instantiating account objects	   *
 * disregarding the type of account		   *
 ******************************************/

public abstract class Account extends StorageAbstract {
	//TODO: if for teacher data are stored as a Map too, put it here as
	//protected Map<String, String> accountData = new HashMap<String, String>();
	//TODO: Possible abstracted constructor:
	//Actually should do the same with user, to make it compact, even though this would mean trusting validation too much
	//public Account(String accountId, Map<String, String> accountData, Map<String, Object> additionalData);
	//TODO: consider putting delete here, since it's basically the same, and depending on TODO1, retrieve too
}