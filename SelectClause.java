//Select Condition

import java.util.ArrayList;
import java.util.Arrays;

public class SelectClause
{
	FileToArray mfstruct;
	ArrayList<String> FirstConVects = new ArrayList<String>();
	ArrayList<String> FirstHaving = new ArrayList<String>();
	ArrayList<PairingTool<Integer, String>> FirstJavaCodeConditions = new ArrayList<PairingTool<Integer, String>>();
	DBMeta info;
	
	//Constructor
	public SelectClause(FileToArray file, DBMeta info_Orig)
	{
		mfstruct = file;
		this.info = info_Orig;
		this.FirstConVects = file.FirstConditions;
		this.FirstHaving = file.FirstHaving;
		this.setCodeList();
		this.setCodeList2();
	}
	
	//set code list
	private void setCodeList()
	{
		for(int i = 0; i != this.FirstConVects.size(); i++)
		{
			String tmpStr = this.FirstConVects.get(i);
			this.setPairIntString(tmpStr);
		}
	}
	
	private void setCodeList2()
	{
		for(int i = 0; i != this.FirstHaving.size(); i++)
		{
			String tmpStr2 = this.FirstHaving.get(i);
			this.setPairIntString2(tmpStr2);
		}
	}
	//set integer string pair
	private void setPairIntString(String tmpStr)
	{		
		int target = -1;
		String finish_parsedStr = new String();
		
		String parsedStr = tmpStr;
		
		parsedStr = parsedStr.replace('\'', '\"');

		for(int i1 = 0; i1 != info.getList().size(); i1++)
		{
			String subName = info.getList().get(i1).getFirst();
			parsedStr = parsedStr.replaceAll(subName, subName + "Tmp");
		}
		
		
		for(int i = 0; i != tmpStr.length(); i++)
		{
			if (tmpStr.charAt(i) == '.')
			{
				String tmp = tmpStr.substring(0,i);
				target = Integer.parseInt(tmp);
				tmp = tmp+"\\.";
				parsedStr = parsedStr.replaceAll(tmp,"");	
				break;
			}
		}	
		
		String[] tempStrings = parsedStr.split(" "); 
		ArrayList<String> strings = new ArrayList<String>(Arrays.asList(tempStrings));
		
		// Deal with and, or, '='...
		for(int j = 0; j != strings.size(); j++)
		{
			String tempStr = strings.get(j);
			if (tempStr.equals("and"))
			{
				finish_parsedStr += "&&";
			}
			else if (tempStr.equals("or"))
			{
				finish_parsedStr += "||";
			}
			else if (tempStr.contains("="))
			{
				String type = new String();
				for(int i2 = 0; i2 != info.getList().size(); i2++)
				{
					String subName = info.getList().get(i2).getFirst();
					if (tempStr.contains(subName))
					{
						type = info.getTypeFromColumn(subName);
					}
				}
				if(!tempStr.contains(">") && 
						!tempStr.contains("<") &&
						!tempStr.contains("!"))
				{
					if (type.equals("String"))
					{
						tempStr = tempStr.replaceFirst("=", ".equalsIgnoreCase(");
						tempStr += ")";
					}
					else
					{
						tempStr = tempStr.replaceFirst("=", "==");
					}
				}
				else if (tempStr.contains("!"))
				{
					if (type.equals("String"))
					{
						tempStr = tempStr.replaceFirst("!=", ".equalsIgnoreCase(");
						tempStr += ")";
						tempStr = "!" + tempStr;
					}
				}
				finish_parsedStr += tempStr;
			}
			else 
			{		
				finish_parsedStr += tempStr;
			}
		}
		this.FirstJavaCodeConditions.add(new PairingTool<Integer, String>(target, finish_parsedStr));
	}
	
	
	private void setPairIntString2(String tmpStr2)
	{		
		int target = -1;
		String finish_parsedStr = new String();
		
		String parsedStr = tmpStr2;
		
		parsedStr = parsedStr.replace('\'', '\"');

		for(int i1 = 0; i1 != info.getList().size(); i1++)
		{
			String subName = info.getList().get(i1).getFirst();
			parsedStr = parsedStr.replaceAll(subName, subName + "Tmp");
		}
		
		
		for(int i = 0; i != tmpStr2.length(); i++)
		{
			if (tmpStr2.charAt(i) == '.')
			{
				String tmp = tmpStr2.substring(0,i);
				target = Integer.parseInt(tmp);
				tmp = tmp+"\\.";
				parsedStr = parsedStr.replaceAll(tmp,"");	
				break;
			}
		}	
		
		String[] tempStrings = parsedStr.split(" "); 
		ArrayList<String> strings = new ArrayList<String>(Arrays.asList(tempStrings));
		
		// Deal with and, or, '='...
		for(int j = 0; j != strings.size(); j++)
		{
			String tempStr = strings.get(j);
			if (tempStr.equals("and"))
			{
				finish_parsedStr += "&&";
			}
			else if (tempStr.equals("or"))
			{
				finish_parsedStr += "||";
			}
			else if (tempStr.contains("="))
			{
				String type = new String();
				for(int i2 = 0; i2 != info.getList().size(); i2++)
				{
					String subName = info.getList().get(i2).getFirst();
					if (tempStr.contains(subName))
					{
						type = info.getTypeFromColumn(subName);
					}
				}
				if(!tempStr.contains(">") && 
						!tempStr.contains("<") &&
						!tempStr.contains("!"))
				{
					if (type.equals("String"))
					{
						tempStr = tempStr.replaceFirst("=", ".equalsIgnoreCase(");
						tempStr += ")";
					}
					else
					{
						tempStr = tempStr.replaceFirst("=", "==");
					}
				}
				else if (tempStr.contains("!"))
				{
					if (type.equals("String"))
					{
						tempStr = tempStr.replaceFirst("!=", ".equalsIgnoreCase(");
						tempStr += ")";
						tempStr = "!" + tempStr;
					}
				}
				finish_parsedStr += tempStr;
			}
			else 
			{		
				finish_parsedStr += tempStr;
			}
		}
		this.FirstJavaCodeConditions.add(new PairingTool<Integer, String>(target, finish_parsedStr));
	}
}