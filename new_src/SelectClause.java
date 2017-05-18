//Select Condition

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectClause
{
	FileToArray mfstruct;
	ArrayList<String> FirstConVects = new ArrayList<String>();
	ArrayList<String> FirstHaveCon = new ArrayList<String>();
	ArrayList<String> FV = new ArrayList<String>();
	ArrayList<PairingTool<Integer, String>> FirstJavaCodeConditions = new ArrayList<PairingTool<Integer, String>>();
	ArrayList<PairingTool<Integer, String>> SecondJavaCodeConditions = new ArrayList<PairingTool<Integer, String>>();
	DBMeta info;
	
	//Constructor
	public SelectClause(FileToArray file, DBMeta info_Orig)
	{
		mfstruct = file;
		this.info = info_Orig;
		this.FirstConVects = file.FirstConditions;
		this.FirstHaveCon = file.FirstHaving;
		this.FV = file.FirstFV;
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
		System.out.println("Here at setCodeList2");
		for(int i = 0; i != this.FirstHaveCon.size(); i++)
		{
			String tmpStr2 = this.FirstHaveCon.get(i);
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
		String finish_parsedStr = new String();
		
		String parsedStr = tmpStr2;
		
		parsedStr = parsedStr.replace('\'', '\"');
		
		ArrayList<String> theList = new ArrayList<String>();
		
		for(int i1 = 0; i1 != info.getList().size(); i1++)
		{
			theList.add(info.getList().get(i1).getFirst());
		}
		
		theList.addAll(FV);
		ArrayList<String> matches = printMatches(parsedStr, "\\b\\w*");
		theList.retainAll(matches);
		
		for(int i1 = 0; i1 != theList.size(); i1++)
		{
			String subName = theList.get(i1);
			String afterName = convertVariableName(theList.get(i1));
			parsedStr = parsedStr.replaceAll(subName, "MFTmp." + afterName.replaceAll("count\\D*", "count_"));
		}

		for(int i = 0; i != tmpStr2.length(); i++)
		{
			if (tmpStr2.charAt(i) == '.')
			{
				String tmp = tmpStr2.substring(0,i);
				tmp = tmp + "\\.";
				parsedStr = parsedStr.replaceAll(tmp,"");	
				break;
			}
		}
		
		String[] tempStrings = parsedStr.split(" ");
		
		ArrayList<String> strings = new ArrayList<String>(Arrays.asList(tempStrings));
		
		for(int i = 0; i < strings.size(); i++) {
			System.out.println(strings.get(i));
		}
		
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
		this.SecondJavaCodeConditions.add(new PairingTool<Integer, String>(0, finish_parsedStr));
	}
	
	private static ArrayList<String> printMatches(String text, String regex) {
		Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(text);
	    // Check all occurrences
	    ArrayList<String> matches = new ArrayList<String>();
	    while (matcher.find()) {
	        System.out.println(" Found: " + matcher.group());
	        matches.add(matcher.group());
	    }
	    return matches;
	}
	
	// Convert 1_avg_quant to avg_quant_1
	private String convertVariableName(String name)
	{
		String numberStr = null;
		if(Character.isDigit(name.charAt(0)) )
		{
			int j = 0;
			for(int i = 0; i != name.length(); i++)
			{
				if(name.charAt(i) == '_')
				{
					j = i;
					break;
				}
			}
			numberStr = name.substring(0, j);
			name = name.substring(j + 1, name.length());
		}
		if(numberStr != null)
		{
			name += "_";
			name += numberStr;
		}
		return name;
	}
}