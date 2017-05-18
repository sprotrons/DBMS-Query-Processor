//Take original file input and save into array lists
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;


public class FileToArray
{
	//Store data from file
	public int numGroupingVari = 0;
	public ArrayList<String> FirstSelectAttr = new ArrayList<String>();
	public ArrayList<String> FirstGroupingAttr = new ArrayList<String>();
	public ArrayList<String> FirstFV = new ArrayList<String>();
	public ArrayList<String> FirstConditions = new ArrayList<String>();
	public ArrayList<String> FirstHaving = new ArrayList<String>();
	
	public FileToArray(FileToArray file)
	{
		this.FirstFV = file.FirstFV;
		this.FirstConditions = file.FirstConditions;
		this.FirstSelectAttr = file.FirstSelectAttr;
		this.numGroupingVari = file.numGroupingVari;
		this.FirstGroupingAttr = file.FirstGroupingAttr;
		this.FirstHaving = file.FirstHaving;
	}
	
	//Override Constructor
	public FileToArray()
	{
		this.FirstSelectAttr = new ArrayList<String>();
		this.numGroupingVari = 0;
		this.FirstGroupingAttr = new ArrayList<String>();
		this.FirstFV = new ArrayList<String>();
		this.FirstConditions = new ArrayList<String>();
		this.FirstHaving = new ArrayList<String>();
	}

	//Set select attri in lists
	public void setSelectAttributes(String currentLine)
	{
		int j = 0;
		for(int i = 0; i != currentLine.length(); i++)
		{
			if(currentLine.charAt(i) == ',')
			{
				if(j == 0)
				{
					String strAttr = currentLine.substring(j, i);
					this.FirstSelectAttr.add(strAttr);
					j = i;
				}
				else
				{
					while(currentLine.charAt(j) == ',' || currentLine.charAt(j) == ' ')
					{
						j++;
					}
					String strAttr = currentLine.substring(j, i);
					this.FirstSelectAttr.add(strAttr);
					j = i;
				}
			}
			else if(i == currentLine.length() - 1)
			{
				while(currentLine.charAt(j) == ',' || currentLine.charAt(j) == ' ')
				{
					j++;
				}
				String strAttr = currentLine.substring(j, i + 1);
				this.FirstSelectAttr.add(strAttr);
			}
		}
		FirstSelectAttr = this.toLowerCase(FirstSelectAttr);
	}
	
	//read from file and set select into lists
	public void setSelectAttributes(BufferedReader br, String currentLine)
	{
		try
		{
			currentLine = br.readLine();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		int j = 0;
		for(int i = 0; i != currentLine.length(); i++)
		{
			if(currentLine.charAt(i) == ',')
			{
				if(j == 0)
				{
					String strAttr = currentLine.substring(j, i);
					this.FirstSelectAttr.add(strAttr);
					j = i;
				}
				else
				{
					while(currentLine.charAt(j) == ',' || currentLine.charAt(j) == ' ')
					{
						j++;
					}
					String strAttr = currentLine.substring(j, i);
					this.FirstSelectAttr.add(strAttr);
					j = i;
				}
			}
			else if(i == currentLine.length() - 1)
			{
				while(currentLine.charAt(j) == ',' || currentLine.charAt(j) == ' ')
				{
					j++;
				}
				String strAttr = currentLine.substring(j, i + 1);
				this.FirstSelectAttr.add(strAttr);
			}
		}
		FirstSelectAttr = this.toLowerCase(FirstSelectAttr);
	}
	
	//Set number of grouping attributes
	public void setGroupingAttrNumber(String currentLine)
	{
		//Check output
		this.numGroupingVari = Integer.parseInt(currentLine);
	}
	
	//Set grouping attributes into list
	public void setGroupingAttrs(String currentLine)
	{
		int j = 0;
		for(int i = 0; i != currentLine.length(); i++)
		{
			if(currentLine.charAt(i) == ',')
			{
				if(j == 0)
				{
					String strAttr = currentLine.substring(j, i);
					this.FirstGroupingAttr.add(strAttr);
					j = i;
				}
				else
				{
					while(currentLine.charAt(j) == ',' || currentLine.charAt(j) == ' ')
					{
						j++;
					}
					String strAttr = currentLine.substring(j, i);
					this.FirstGroupingAttr.add(strAttr);
					j = i;
				}
			}
			else if(i == currentLine.length() - 1)
			{
				while(currentLine.charAt(j) == ',' || currentLine.charAt(j) == ' ')
				{
					j++;
				}
				String strAttr = currentLine.substring(j, i + 1);
				this.FirstGroupingAttr.add(strAttr);
			}
		}
		FirstGroupingAttr = this.toLowerCase(FirstGroupingAttr);
	}
	
	//read from file, set grouping attributes
	public void setGroupingAttrs(BufferedReader br, String currentLine)
	{
		try
		{
			currentLine = br.readLine();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		int j = 0;
		for(int i = 0; i != currentLine.length(); i++)
		{
			if(currentLine.charAt(i) == ',')
			{
				if(j == 0)
				{
					String strAttr = currentLine.substring(j, i);
					this.FirstGroupingAttr.add(strAttr);
					j = i;
				}
				else
				{
					while(currentLine.charAt(j) == ',' || currentLine.charAt(j) == ' ')
					{
						j++;
					}
					String strAttr = currentLine.substring(j, i);
					this.FirstGroupingAttr.add(strAttr);
					j = i;
				}
			}
			else if(i == currentLine.length() - 1)
			{
				while(currentLine.charAt(j) == ',' || currentLine.charAt(j) == ' ')
				{
					j++;
				}
				String strAttr = currentLine.substring(j, i + 1);
				this.FirstGroupingAttr.add(strAttr);
			}
		}
		FirstGroupingAttr = this.toLowerCase(FirstGroupingAttr);
	}
	
	//Set F vect into list
	public void setFV(String currentLine)
	{
		int j = 0;
		for(int i = 0; i != currentLine.length(); i++)
		{
			if(currentLine.charAt(i) == ',')
			{
				if(j == 0)
				{
					String strAttr = currentLine.substring(j, i);
					this.FirstFV.add(strAttr);
					j = i;
				}
				else
				{
					while(currentLine.charAt(j) == ',' || currentLine.charAt(j) == ' ')
					{
						j++;
					}
					String strAttr = currentLine.substring(j, i);
					this.FirstFV.add(strAttr);
					j = i;
				}
			}
			else if(i == currentLine.length() - 1)
			{
				while(currentLine.charAt(j) == ',' || currentLine.charAt(j) == ' ')
				{
					j++;
				}
				String strAttr = currentLine.substring(j, i + 1);
				this.FirstFV.add(strAttr);
			}
		}
		FirstFV = this.toLowerCase(FirstFV);
	}
	
	//read from file, set F vect into list
	public void setFV(BufferedReader br, String currentLine)
	{
		try
		{
			currentLine = br.readLine();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		int j = 0;
		for(int i = 0; i != currentLine.length(); i++)
		{
			if(currentLine.charAt(i) == ',')
			{
				if(j == 0)
				{
					String strAttr = currentLine.substring(j, i);
					this.FirstFV.add(strAttr);
					j = i;
				}
				else
				{
					while(currentLine.charAt(j) == ',' || currentLine.charAt(j) == ' ')
					{
						j++;
					}
					String strAttr = currentLine.substring(j, i);
					this.FirstFV.add(strAttr);
					j = i;
				}
			}
			else if(i == currentLine.length() - 1)
			{
				while(currentLine.charAt(j) == ',' || currentLine.charAt(j) == ' ')
				{
					j++;
				}
				String strAttr = currentLine.substring(j, i + 1);
				this.FirstFV.add(strAttr);
			}
		}
		FirstFV = this.toLowerCase(FirstFV);
	}
	
	//Set conditions into list
	public void setConditions(String currentLine)
	{
		int j = 0;
		for(int i = 0; i != currentLine.length(); i++)
		{
			if(currentLine.charAt(i) == ',')
			{
				if(j == 0)
				{
					String strAttr = currentLine.substring(j, i);
					this.FirstConditions.add(strAttr);
					j = i;
				}
				else
				{
					while(currentLine.charAt(j) == ',' || currentLine.charAt(j) == ' ')
					{
						j++;
					}
					String strAttr = currentLine.substring(j, i);
					this.FirstConditions.add(strAttr);
					j = i;
				}
			}
			else if(i == currentLine.length() - 1)
			{
				while(currentLine.charAt(j) == ',' || currentLine.charAt(j) == ' ')
				{
					j++;
				}
				String strAttr = currentLine.substring(j, i + 1);
				this.FirstConditions.add(strAttr);
			}
		}
		FirstConditions = this.toLowerCase(FirstConditions);
	}
	
	//Read from file, set conditions into list
	public void setConditions(BufferedReader br, String currentLine)
	{
		try
		{
			currentLine = br.readLine();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		int j = 0;
		for(int i = 0; i != currentLine.length(); i++)
		{
			if(currentLine.charAt(i) == ',')
			{
				if(j == 0)
				{
					String strAttr = currentLine.substring(j, i);
					this.FirstConditions.add(strAttr);
					j = i;
				}
				else
				{
					while(currentLine.charAt(j) == ',' || currentLine.charAt(j) == ' ')
					{
						j++;
					}
					String strAttr = currentLine.substring(j, i);
					this.FirstConditions.add(strAttr);
					j = i;
				}
			}
			else if(i == currentLine.length() - 1)
			{
				while(currentLine.charAt(j) == ',' || currentLine.charAt(j) == ' ')
				{
					j++;
				}
				String strAttr = currentLine.substring(j, i + 1);
				this.FirstConditions.add(strAttr);
			}
		}
		FirstConditions = this.toLowerCase(FirstConditions);
	}
	
	// having conditions
	public void setHaving(String currentLine)
		{
			int j = 0;
			for(int i = 0; i != currentLine.length(); i++)
			{
				if(currentLine.charAt(i) == ',')
				{
					if(j == 0)
					{
						String strAttr = currentLine.substring(j, i);
						this.FirstHaving.add(strAttr);
						j = i;
					}
					else
					{
						while(currentLine.charAt(j) == ',' || currentLine.charAt(j) == ' ')
						{
							j++;
						}
						String strAttr = currentLine.substring(j, i);
						this.FirstHaving.add(strAttr);
						j = i;
					}
				}
				else if(i == currentLine.length() - 1)
				{
					while(currentLine.charAt(j) == ',' || currentLine.charAt(j) == ' ')
					{
						j++;
					}
					String strAttr = currentLine.substring(j, i + 1);
					this.FirstHaving.add(strAttr);
				}
			}
			FirstHaving = this.toLowerCase(FirstHaving);
			for(int i = 0; i != FirstHaving.size(); i++)
			{
				System.out.println(FirstHaving.get(i));
				System.out.println(FirstHaving.size());
			}
		}
		
	// having conditions file version
	public void setHaving(BufferedReader br, String currentLine) 
	{
			try
			{
				currentLine = br.readLine();
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			try {
				do
				{
					int j = 0;
					for(int i = 0; i != currentLine.length(); i++)
					{
						if(currentLine.charAt(i) == ',')
						{
							if(j == 0)
							{
								String strAttr = currentLine.substring(j, i);
								this.FirstHaving.add(strAttr);
								j = i;
							}
							else
							{
								while(currentLine.charAt(j) == ',' || currentLine.charAt(j) == ' ')
								{
									j++;
								}
								String strAttr = currentLine.substring(j, i);
								this.FirstHaving.add(strAttr);
								j = i;
							}
						}
						else if(i == currentLine.length() - 1)
						{
							while(currentLine.charAt(j) == ',' || currentLine.charAt(j) == ' ')
							{
								j++;
							}
							String strAttr = currentLine.substring(j, i + 1);
							this.FirstHaving.add(strAttr);
						}
					}
				}
				while((currentLine = br.readLine()) != null);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			FirstHaving = this.toLowerCase(FirstHaving);
			for(int i = 0; i != FirstHaving.size(); i++)
			{
				System.out.println(FirstHaving.get(i));
				System.out.println(FirstHaving.size());
			}
		}

	//Set all to lowercase since sql is not case sensitive
	private ArrayList<String> toLowerCase(ArrayList<String> First)
	{
		for (int i = 0; i != First.size(); i++)
		{
			First.set(i, First.get(i).toLowerCase());
		}
		return First;
	}
}