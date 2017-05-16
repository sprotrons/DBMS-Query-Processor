//Interface data storage

public class InterfaceInfo
{
	String strDBUserName = new String();
	String strDBPSW = new String();
	String strDBURL = new String();
	String strTableName = new String();
	
	public InterfaceInfo()
	{
		strDBURL = "jdbc:postgresql://localhost:5432/sales";
		strDBUserName = "postgres";
		strDBPSW = "supersuper13";
		strTableName = "sales";
	}
	
	public InterfaceInfo(InterfaceInfo file)
	{
		this.strDBPSW = file.strDBPSW;
		this.strDBURL = file.strDBURL;
		this.strDBUserName = file.strDBUserName;
		this.strTableName = file.strTableName;
	}
}