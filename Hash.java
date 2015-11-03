import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileNotFoundException;



public class Hash {
    /* Author: DUstin Hu
       Date: 11-11-2014
       Purpose: To be the main

       Methods: 
           main: To be the main
    */

    public static void main(String[] args) throws IOException, FileNotFoundException{
	// Author: DUstin Hu
	// Date: 21-11-2014
	// Purpoes: To demonstarte the functonality of the HashDataBase
	// Input: None
	// Output: None
	HashDataBase somethingElse = new HashDataBase("videodb.csv", 30);
	System.out.println("Created hash database of size 30, current contents are:");

	System.out.println("");
	

	System.out.println(somethingElse.physicalSize);
	System.out.println(somethingElse.logicalSize);
	System.out.println("Adding 150 elements from file");
	for (int i = 0; i < 150; i++){
	    somethingElse.add();
	}

	somethingElse.peek();

	System.out.println("Is this hash database full?");
	System.out.println(somethingElse.isFull());
	System.out.println("");
	
	System.out.println("Searching for record with an id of 15");
	System.out.println(somethingElse.search(15));
	System.out.println("Searching for record with an id of 2627");
	System.out.println(somethingElse.search(2627));
	System.out.println("Searching for record with an id of 2807");
	System.out.println(somethingElse.search(2807));
	System.out.println("Searching for record with an id of 2859");
	System.out.println(somethingElse.search(2859));
	System.out.println("Searching for record with an id of 2932");
	System.out.println(somethingElse.search(2932));
	
	System.out.println("Finding a record" );
	somethingElse.find();

	System.out.println("Deleting a record");
	somethingElse.delete();

	somethingElse.peek();
	
	System.out.println("");
	somethingElse.change();
	somethingElse.peek();
    }
}


class DBRecord{
    /*
      Author: DUstin Hu
      Date: 11-11-2014
      Purpose: To hold the record 
      
      Fields:
          id: The record id
	  title: The title of the record
	  type: THe type of movie
	  cost: The cost of the movie
	  dist: The distance to travel
	  date: Date of record

      Methods:
          constructor: Creates the DBR given nothing
          constructor: Creates the DBR given a single string
          constructor: Creates the DBRRecord given all the information
	  toString: ALigns information in a string
	  rightPad: Pads a string with spaces on the left to a certain width
	  
       
     */

    public int id;

    protected double cost; 

    protected String title;
    protected String type;
    protected String dist;
    protected String date;

    public DBRecord(){
	// Author: Dustin Hu
	// Date: 18-11-2014
	// Purpose: To cerate the record
	// Ipnut: None
	// Output: None
	this.id = -1;
	this.cost = 0;
	this.title = "";
	this.type = "";
	this.dist = "";
	this.date = "";
    }

    public DBRecord(String input ){
	// Author: Dustin Hu
	// Date: 13-11-2014
	// Purpose:To create the record
	// Input: The string to create a record from
	// OUtput: None
	
	this.id = Integer.parseInt(input.substring(0, input.indexOf(",")));
	input = input.substring(input.indexOf(",") + 1);
	
	this.title = input.substring(0, input.indexOf(","));
	input = input.substring(input.indexOf(",") + 1);
	
	this.type = input.substring(0, input.indexOf(","));
	input = input.substring(input.indexOf(",") + 1);
	
	this.cost = Double.parseDouble(input.substring(0, input.indexOf(",")));
	input = input.substring(input.indexOf(",") + 1);

	this.dist = input.substring(0, input.indexOf(","));
	input = input.substring(input.indexOf(",") + 1);
	
	this.date = input;
    }

    public DBRecord(int x, String title, String type, 
		    double cost, String dist, String date){
	// Author: Mr. Smith
	// Date: 11-11-2014
	// Purpose: To create the record
	// Input: The id, the title, the type, the cost, the distance, and the date
	// OUtput: None
	this.id = x;
	this.title = title;
	this.type = type;
	this.cost = cost;
	this.dist = dist;
	this.date = date;
    }

    public String toString(){
	// Date: 11-11-2014
	// Author: Mr. Smith
	// Purpose: To return the string
	// Input: None
	// Output: The data fields of the DBRecord
	
	return (rightPad(""+  this.id, 8) + 
		rightPad(this.title, 50) +
		rightPad(this.type, 10) + 
		rightPad("" + this.cost, 8) + 
		rightPad(dist, 6) + 
		rightPad(date, 12));
    }

    public String rightPad (String x, int w){
	// Date: 11-11-2014
	// Author: Mr. Smith
	// Purpose: To pad the string with a certain number of spaces
	// Input: The string to pad and the numbero f spaces to pad by
	// Output: The padded string
	
	String s = "" + x;
	while (s.length() < w){
	    s = s + " ";
	}
	
	return s;
    }

    
}
   
class HashDataBase {
    /*
      Author: DustinHu
      Date: 11-11-2014
      Purpose: To hold all the values

      Fields:
          textInput: The file conatining the input
	  dataBase: The array containing the records
	  physicalSize: The size of the array, an integer
	  logicalSize: The number of records in the array

      Methods:
          constructor: Creates the array given a size
	  isPrime: Checks if a number is prime
	  findNextPrime: Gets the next prime
	  isFull: Checks if the database is full
	  hash: To hash the id
	  doubleHash: The double hashing function
	  rehash: Creates a new table that is roughly 20% larger 
	  insert: Inserts a record
	  search: Searches for a record
	  add: adds a record
	  find: Finds a record
	  delete: Deletes the record, given the id
	  delete: Deletes the record
	  change: Changes a record
	  rightPad: Pads the string
	  deleteHash: Rehashes for delete
     */

    protected BufferedReader textInput;
    protected DBRecord [] database;
    protected int physicalSize;
    protected int logicalSize;




    public HashDataBase (String filename, int size) throws FileNotFoundException{
	// Author: Dustin Hu
	// Date: 12-11-2014
	// Purpose: To create the HashDataBase
	// Input: The file to open and the physical size
	// Output: None

	this.textInput = new BufferedReader(new FileReader(filename));
	if (isPrime(size) == false ){
	    this.physicalSize = findNextPrime(size);

	}
	else{
	    this.physicalSize = size;
	}
	this.logicalSize = 0;

	this.database = new DBRecord[physicalSize];


    
    }
    public boolean isPrime(int x){
	// AUthor: DUstin Hu
	// Date: 10-09-2014
	// Purpose: To check whether or not a number is prime
	// Input: An integer x
	// Output: A boolean true or false
	boolean prime = false;
	boolean divided = false;

	int i = 3;

	if (x == 2 || x == 3){
	    prime = true;
	}
	else if (x < 2 || x % 2 == 0){
	    prime = false;
	}
	else{
	    while (i <= Math.sqrt(x) && divided == false){
		if (x % i == 0){
		    divided = true;
		    prime = false;
		}
		else{
		    i = i + 2;
		}
	    }
	    if (i >= Math.sqrt(x) && divided == false) {
		prime = true;
	    }
	}

	return prime;
    }
    public int findNextPrime(int x){
	// AUthor: Dustin Hu
	// Date: 10-09-2014
	// Purpose: TO find the next prime number
	// Input: An integer x
	// OUtput: AN integer, the next prime number.
	x = x + 1;
	while (isPrime(x) == false){
	    x = x + 1;
	}
	return x;
    }
    public boolean isFull(){
	// Author: Dustin Hu
	// Date: 12-11-2014
	// Purpose: To check whether or not the database is full
	// Input: None
	// Output: Boolean true if logicalSize/physicalSize > 70%

	boolean full;
	double fullness = (double) ((double) this.logicalSize / (double) this.physicalSize);
	if (fullness >= 0.7){
	    full = true;
	}
	else{
	    full = false;
	}
	return full;
    }
    public int hash (int k, int i){
	// Author: Mr. Smith
	// Date: 12-11-2014
	// Purpose: To hash the id
	// Input: The id and the number of times i'ts been hashed
	// Output: The hashed id
	return (k % this.physicalSize + i * doubleHash(k)) % this.physicalSize;
    }
    public int doubleHash(int k){
	// Author: Mr. Smith
	// Date: 12-11-2014
	// Purpose: To double hash
	// Input: The id of the record
	// Output: The doubly hashed id
	return (this.physicalSize - 2) - (k % (this.physicalSize - 2));
    }
    public void rehash(){
	// Author: Dusitn Hu
	// Date: 13-11-2014
	// Purpose: To create a new new table with a prime size approximately 20% larger and reinserting all the old, non-deleted records back into it.
	// Input: none
	// Output: None
	int newSize = findNextPrime((int) (this.physicalSize * 1.2));
	int currentPosition;
	int k = 0;

	DBRecord[] oldTable = this.database;
	DBRecord oldRecord;

	this.database = new DBRecord [newSize];
	this.physicalSize = this.database.length;


	for (int i = 0; i < oldTable.length; i++){
	    if (oldTable[i] != null){
		oldRecord = oldTable[i];
		currentPosition = this.hash(oldRecord.id, 0);
		k = 0;
		while (this.database[currentPosition] != null){
		    k = k + 1;
		    currentPosition = this.hash(oldRecord.id, k);
		}
		this.database[currentPosition] = oldRecord;
	    }
	    // if (oldTable[i] != null){
	    // 	currentPosition = hash(oldTable[i].id, 0);
	    // 	k = 0;
	    // 	while (newTable[currentPosition] != null){
	    // 	    k = k + 1;
	    // 	    currentPosition = hash(oldTable[i].id, k);
	    // 	}
	    // 	newTable[currentPosition] = oldTable[i];
	    // }
	}


	
    }
    public void peek (){
	// Author: Dustin Hu
	// Date: 13-11-2014
	// Purpose: To peek at the hashtable
	// Input: None
	// Output: None

	String position;
	for (int i = 0; i < this.database.length; i++){

	    if (i < 10){
		position = rightPad(String.valueOf(i), 4);
	    }
	    else if (i < 100 && i > 9){
		position = rightPad(String.valueOf(i), 4);
	    }
	    else if (i < 1000 && i > 99){
		position = rightPad(String.valueOf(i), 4);
	    }
	    else{
		position = rightPad(String.valueOf(i), 4);
	    }
		
	    System.out.print(position + ": ");
	    if (this.database[i] != null){
		System.out.println(this.database[i].toString());
	    }
	    else{
		System.out.println("Null");
	    }

	}
    }
    public void insert(DBRecord x){
	// AUthor: Dustin Hu
	// Date: 13-11-2014
	// Purpose: To insert the record
	// Input: THe record to input
	// Output: None
	
	int position = hash(x.id, 0);
	int k = 0; 
	this.logicalSize = this.logicalSize + 1;
	
	if (this.isFull() == false){
	    while (this.database[position] != null){
		position = hash(x.id, k);
		k = k + 1;
		
	    }
	    this.database[position] = x;
	}
	else{
	    this.rehash();
	    while (this.database[position] != null){
		position = hash(x.id, k);
		k = k + 1;
	    }
	    this.database[position] = x;
	    System.out.println(x.toString());
	}

    }
    
    public int search(int id){
    	// Author: Dustin Hu
    	// Date: 17-11-2014
    	// Purpose: To search for a record
    	// Input: THe id to look for
    	// Output: The position of the id
	
	int position = hash(id, 0);
	int k = 0; 
	boolean found = false;

	while (found == false){
	    if (this.database[position] == null){
		found = true;
		position = -1;
	    }
	    else if (this.database[position].id == id)  {
		found  = true;
	    }
	    else{
		k = k + 1;
		position = hash (id, k);
	    }
	}
    	// boolean found = false;

    	// int output = hash(id, 0);
    	// int k = 0;
	// System.out.println(output);
    	// while (found == false){
    	//     if (this.database[output] == null){
    	// 	found = true;
    	// 	output = -1;

    	//     }
    	//     else if (this.database[output].id == id){
    	// 	found = true;
    	//     }
    	//     else{
    	// 	k = k + 1;
    	// 	output = hash(id, k);
    	//     }
    	// }
	return position;
    }
    public void add() throws IOException{
    	// AUthor: Dustin Hu
    	// Date: 17-11-2014
    	// Purpose: To add a line from the file to the table
    	// Input: None
    	// Output: None
    	String input;
    	DBRecord record;
    	if (this.textInput.ready()){
    	    input = textInput.readLine();
    	    record = new DBRecord(input);
    	    this.insert(record);
    	}

    }
    public void find() throws IOException{
    	// Author: Dustin Hu
    	// Date: 17-11-2014
    	// Purpose: To get the id from the user and to find the record
    	// Input: None
    	// Output: None
    	BufferedReader input = new BufferedReader(
    						  new InputStreamReader(System.in));
    	int id = -1;
	String userInput;
    	int position;
	while (id < 0){
	    System.out.println("Please enter the ID that you wish to look for");
	    System.out.print("> ");
	    userInput = input.readLine().trim();
	    id = Integer.parseInt(String.valueOf(userInput));
	}
    	position = search(id);
    	if (position != -1){
    	    System.out.println(this.database[position].toString());
    	}
    	else{
    	    System.out.println("No such record.");
    	}
	
	
    }
    public void delete(int id){
	// Author: Dustin Hu
	// Date: 17-11-2014
	// Purpose: To delete the record
	// Input: The ID to delete
	// Output: None

	int position = search(id);
	if (position != -1){
	    this.database[position] = null;
	    this.logicalSize = this.logicalSize - 1;
	    this.deleteHash();
	}

    }
    public void delete() throws IOException{
	// Author: Dustin Hu
	// Date: 17-11-2014
	// Purpose: To delete a record
	// Input: None
	// Output: None
	BufferedReader input = new BufferedReader(
						  new InputStreamReader(System.in));
	int id = -1;
	while (id < 0){
	    System.out.println("What ID do you want to delete?");
	    id = Integer.parseInt(String.valueOf(input.readLine()));
	}
	this.delete(id);
    }
    public void change () throws IOException{
	// Author: Dustin Hu
	// Date: 18-11-2014
	// Purpose: To change a record
	// Input: None
	// Output: None
    	BufferedReader input = new BufferedReader(
    						  new InputStreamReader(System.in));
    	int id = -1;
	String userInput;
    	int position;

	DBRecord temp = new DBRecord();
	DBRecord original;
	while (id < 0){
	    System.out.println("Please enter the ID that you wish to look for");
	    System.out.print("> ");
	    userInput = input.readLine().trim();
	    id = Integer.parseInt(String.valueOf(userInput));
	}
    	position = this.search(id);
    	if (position != -1){
    	    System.out.println(this.database[position].toString());
	    original = this.database[position];

	    System.out.println("New ID? (Hit Enter if you wish for it to remain the same)");

	    position = id;
	    userInput = input.readLine();
	    if (userInput.equals("")){
		temp.id = original.id;

	    }
	    else{
		temp.id = Integer.parseInt(userInput);
	    }

	    System.out.println("New title? (Hit Enter if you wish for it to remain the same)");
	    
	    userInput = input.readLine().trim();
	    if (userInput.equals("") ){
		temp.title = original.title;
	    }
	    else{
		temp.title = userInput;

	    }

	    System.out.println("New genre? (Hit Enter if you wish for it to remain the same)");
	    
	    userInput = input.readLine().trim();
	    if (userInput.equals("") ){
		temp.type = original.type;

	    }
	    else{
		temp.type = userInput;
	    }

	    System.out.println("New cost? (Hit Enter if you wish for it to remain the same)");
	    
	    userInput = input.readLine().trim();

	    if (userInput.equals("")){
		temp.cost = original.cost;

	    }
	    else{
		temp.cost = Double.parseDouble(userInput);
	    }

	    System.out.println("New distance? (Hit Enter if you wish to for it to remain the same)");
	    userInput = input.readLine();
	    if (userInput.equals("")){
		temp.dist = original.dist;
	    }
	    else{

		temp.dist = userInput;
	    }

	    System.out.println("New date? (Hit Enter if you wish for it to remain the same)");

	    userInput = input.readLine();
	    if (userInput.equals("")){
		temp.date = original.date;
	    }
	    else{
		temp.date = userInput;

	    }

	    if (temp.id == original.id){
		original.title = temp.title;
		original.type = temp.type;
		original.cost = temp.cost;
		original.dist = temp.dist;
		original.date = temp.date;
	    }
	    else{
		this.delete(original.id);
		this.insert(temp);
	    }


    	}
    	else{
    	    System.out.println("No such record.");
    	}
	
    }

    public String rightPad (String x, int w){ 
	// Date: 11-11-2014
	// Author: Mr. Smith
	// Purpose: To pad the string with a certain number of spaces
	// Input: The string to pad and the numbero f spaces to pad by
	// Output: The padded string
	
	String s = "" + x;
	while (s.length() < w){
	    s = s + " ";
	}
	
	return s;
    }
    public void deleteHash(){
	// Author: Dusitn Hu
	// Date: 13-11-2014
	// Purpose: To create a new new table with a prime size approximately 20% larger and reinserting all the old, non-deleted records back into it.
	// Input: none
	// Output: None
	int newSize = this.physicalSize;
	int currentPosition;
	int k = 0;

	DBRecord[] oldTable = this.database;
	DBRecord oldRecord;

	this.database = new DBRecord [newSize];
	this.physicalSize = this.database.length;


	for (int i = 0; i < oldTable.length; i++){
	    if (oldTable[i] != null){
		oldRecord = oldTable[i];
		currentPosition = this.hash(oldRecord.id, 0);
		k = 0;
		while (this.database[currentPosition] != null){
		    k = k + 1;
		    currentPosition = this.hash(oldRecord.id, k);
		}
		this.database[currentPosition] = oldRecord;
	    }
	    // if (oldTable[i] != null){
	    // 	currentPosition = hash(oldTable[i].id, 0);
	    // 	k = 0;
	    // 	while (newTable[currentPosition] != null){
	    // 	    k = k + 1;
	    // 	    currentPosition = hash(oldTable[i].id, k);
	    // 	}
	    // 	newTable[currentPosition] = oldTable[i];
	    // }
	}


	
    }
}
