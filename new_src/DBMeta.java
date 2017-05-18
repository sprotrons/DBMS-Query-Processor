//Check metadata of database

import java.util.ArrayList;

public class DBMeta
{
	//Stores the typename pair of each column
	private ArrayList<PairingTool<String, String>> FirstPair = new ArrayList<PairingTool<String, String>>();

	public void addValue(String column, String type)
	{
		if(column != null && type != null)
		{
			PairingTool<String, String> pairColType = new PairingTool<String, String>();
			pairColType.put(column, type);
			this.FirstPair.add(pairColType);
		}
	}	


	public void setStructTypeJAVA()
	{
		for(int i = 0; i != FirstPair.size(); i++)
		{
			String typeOrigName = FirstPair.get(i).getSecond();
			if(typeOrigName.equals("character varying"))
			{
				FirstPair.get(i).setSecond("String");
			}
			else if(typeOrigName.equals("character"))
			{
				FirstPair.get(i).setSecond("String");
			}
			else if(typeOrigName.equals("integer"))
			{
				FirstPair.get(i).setSecond("int");
			}
		}
	}
	
	//Grab type from column
	public String getTypeFromColumn(String column)
	{
		for(int i = 0; i != FirstPair.size(); i++)
		{
			if(FirstPair.get(i).getFirst().equals(column))
			{
				return FirstPair.get(i).getSecond();
			}
		}
		return null;
	}
	
	//Return the pair list
	public ArrayList<PairingTool<String, String>> getList()
	{
		return FirstPair;
	}
}
