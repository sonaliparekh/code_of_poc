package methods;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.mysql.jdbc.Connection;
import java.util.*;

// class for mysql connection..
class init
{
    public static Connection con;
    public static Statement stmt,stmt2;
	public static ResultSet rs,rs2;
	
	public static void initialize() throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3309/jd","root","root"); 
		/*if(con!=null)
			System.out.println("success");*/
	}
}
//class for finding experience and skills
public class parsing {
	int i,j,count, flag=0,count_exp,no;
	int[][] exp=new int[20][2];
	String[] arr;
	String[] skills=new String[10];
	String[] Month={"jan","feb","mar","apr","may","jun","jul","aug","sept","oct","nov","dec","till"};
	String[] month={"january","february","march","april","may","june","july","august","september","october","november","december","till"};
	String[] count_month={"one","two","three","four","five","six","seven","eight","nine","ten","eleven","twelve","thirteen","fourteen","fifteen","sixteen","seventeen","eighteen","ninteen","twenty"};
	Calendar cal = new GregorianCalendar();
	  int m = cal.get(Calendar.MONTH);
	  int year = cal.get(Calendar.YEAR);
	public parsing() throws Exception
	{
		count=-1;
		count_exp=0;
		no=0;
		init.initialize();
		init.stmt=init.con.createStatement();
		
	}
	public void start(String[] fileData) throws SQLException
	{
		
		//for skills
		System.out.println(" Skills matched: ");
		count_skill(fileData);
		print();
		flag=0;
		// for experience
		System.out.println("Experience: ");
		for(int i=0;i<fileData.length;i++)
		{
			arr=fileData[i].split("\n\r");				
			for(int j=0;j<arr.length;j++)
				experience(arr[j]);
		}
		for(i=0;i<count_exp;i++)
		{
			int c_year=year%100;
			if(exp[i][1]==year || exp[i+1][1]==year || exp[i][1]==c_year || exp[i+1][1]==c_year)
			{
				System.out.println(++no + ") Current employer:");
				System.out.println("   " +exp[i][0]+" "+exp[i][1]+" to " +exp[i+1][0]+" "+exp[i+1][1]);
				diff_month(exp[i][0],exp[i][1],exp[i+1][0],exp[i+1][1]);
			}
			else
			{
				System.out.println(++no + ") Other work experience");
				System.out.println("   " +exp[i][0]+" "+exp[i][1]+" to " +exp[i+1][0]+" "+exp[i+1][1]);
				diff_month(exp[i][0],exp[i][1],exp[i+1][0],exp[i+1][1]);
			}
			i++;
		}
	}
	void diff_month(int m1,int y1,int m2,int y2)
	{
		int n_month,n_years;
		int c_year=year%100;
		n_years=y2-y1;
		if(m1>m2)
		{
			n_month=12-m1+m2;
			n_years--;
		}
		else
			n_month=m2-m1;
		if(y1==year || y2==year||y1==c_year || y2==c_year)
			System.out.println("  Working since: " + n_years +" years " +n_month+ " months");
		else 
			System.out.println("  Worked for: " +n_years +" years " +n_month +" months ");
	}
	// function for counting skill.
	void count_skill(String fileData[]) throws SQLException
    {
		int flag=0,k;
		String[] temp=new String[5];
		String skill;
		String [] considered=new String[10];
    	for(i=0;i<fileData.length;i++) // loop for entire data in file.
		{
			arr=fileData[i].split("[\\s-,/]+");
			for(j=0;j<arr.length;j++) // loop for a data in a line..
			{
				k=1;
				flag=0;
				init.rs=init.stmt.executeQuery("select field from skill");
				while(init.rs.next()) // loop for a data in table..
				{
					skill=init.rs.getString(1);
					temp=skill.split("\\s");
					if(temp[0].equals(arr[j].toLowerCase())) // checking if skill present or not..
					{
						if(temp.length!=1) // if skill is having more than one word
						{
							for(k=1;k<temp.length;k++) // checking for next words in skill
							{
								if(temp[k].equals(arr[++j].toLowerCase())==false)
									break;		
							}
						}
						if(k==temp.length)
						{
							for(k=0;k<=count;k++) // checking for skill already considered.
							{
								if(considered[k].equals(skill.toLowerCase()))
								{
									flag=1;
									break;
								}
							}
							if(flag==0) // if not then considered
							{
								++count;
								considered[count]=skill.toLowerCase();
								System.out.println("  "+(count+1)+")"+considered[count]+" ");
							}
							break;
						}//end of if
					}// end of if
				}// end of while
			}// end of for
		}// end of for
    }

    void print()
    {
    	System.out.println("No. of Skills Matched : " +(count+1));
    }
    // function for finding out experience
    void experience(String line)
    {
    	String spl[]=new String[20];
    	if(check(line)==true)
    	{
    		spl=line.split("[:\\s-,()+]+");
    		Pattern p = Pattern.compile( "(([0-9])([0-9]?)(.?)([0-9]?))" );
    		for(i=0;i<spl.length;i++)
    		{
    			Matcher m = p.matcher(spl[i]);
    			if(m.matches()==true) 
    			{   		
    				try
    				{
    					if(spl[i+1].toLowerCase().equals("years") || spl[i+1].toLowerCase().equals("year")|| spl[i+1].toLowerCase().equals("yrs")|| spl[i+1].toLowerCase().equals("yr"))
    					{
    	    			//  return true;
    							System.out.println(spl[i]+" years");
    					}
    					if(spl[i+1].toLowerCase().equals("month") || spl[i+1].toLowerCase().equals("months")||spl[i+1].toLowerCase().equals("month.") || spl[i+1].toLowerCase().equals("months."))
							System.out.println(spl[i]+" months");

    					
    				}
    				catch(ArrayIndexOutOfBoundsException e)
    				{
    				}
    			}
    		}
    		spl=line.split("[:\\s-,()+.]+");
    		check_month(line); // for like feb 2012 to mar 2012
    		// if someone has written like one year..
    		for(j=0;j<spl.length;j++)
    			for(i=0;i<count_month.length;i++)
    			{
    				if(count_month[i].equals(spl[j].toLowerCase()))
    				{
        	    		if(spl[i+1].toLowerCase().equals("years") || spl[i+1].toLowerCase().equals("year")|| spl[i+1].toLowerCase().equals("yrs")|| spl[i+1].toLowerCase().equals("yr"))
    					System.out.println(count_month[i]+ " ");
    					break;		
    				}
    			}
    	}
    }
    void check_month(String line)
    {
    	String[] spl=line.split("[:\\s,'()+.' '’]+");
		int length=spl.length;
		int k;
    	//System.out.println("Length"+spl.length);
    	for(j=0;j<length;j++)
    	{
    		i=month_present(spl[j].toLowerCase());
    		// if month present
    		if(i!=-1)
    		{
    			copy_to_exp(i,j,spl);
    			for(k=j;k<length;k++)
    				if(spl[k].toLowerCase().equals("to")||spl[k].toLowerCase().equals("-")||spl[k].toLowerCase().equals("–")) 
    					break;
    			i=month_present(spl[++k].toLowerCase());
    			if(i!=-1)
    			{
    				copy_to_exp(i,k,spl);
    				j=k;
    			}
    			else
    				count_exp--;
    			break;
    		}
    		else
    		{
    			i=check_year(spl[j]);
    			if(i!=-1)
    			{
    				copy_to_exp(0,j+1,spl);
    				for(k=j;k<length;k++)
    					if(spl[k].toLowerCase().equals("to")||spl[k].toLowerCase().equals("-"))
    						break;
    				i=check_year(spl[++k]);
    					if(i!=-1)
    					{
    						copy_to_exp(0,k,spl);
    						j=k;
    					}
    					else
    						count_exp--;
    					break;
    			}
    		}
    	}
    	//}
		/*for(j=0;j<spl.length;j++)
			for(i=0;i<Month.length;i++)
			{
				if(Month[i].equals(spl[j].toLowerCase()))
				{
					System.out.println(Month[i]+ " ");
					break;
				}
			}*/
    }
    void copy_to_exp(int i,int j,String[] spl)
    {
    	

    	if(month[i].toLowerCase().equals("till") ||month[i].toLowerCase().equals("present"))
		{
    		exp[count_exp][0]=m+1;
			exp[count_exp][1]=year;
		}
		else
		{
			exp[count_exp][0]=i+1;
			i=check_year(spl[++j]);
			if(i!=-1)
				exp[count_exp][1]=i;
			else
				exp[count_exp][1]=year; // replace with system date
		}
		count_exp++;
    }
    int month_present(String name)
    {
    	int i;
    	for(i=0;i<month.length;i++)
		{
			if(month[i].toLowerCase().contentEquals(name)||Month[i].toLowerCase().contentEquals(name))
			{
				return i;
			}
		}
    	return -1;
    }
    int check_year(String name)
    {
    	try
    	{
    		int year=Integer.parseInt(name);
    		int present_yr=year;
    		int pre=year%100; // have to replace with system date
    		if((year>1990 && year<=present_yr) || (year>90 && year<=pre))
    			return year;
    		else
    			return -1;
    	}
    	catch(NumberFormatException e)
    	{
    		return -1;
    	}
    }
    // function to find experience.
    boolean check(String line)
    {
    	String[] arr=new String[30];
    	arr=line.split("[\\s,()./:-]+");
    	for(int i=0;i<arr.length;i++)
    	{
    		if(arr[i].toLowerCase().equals("experience") || arr[i].toLowerCase().equals("work")|| arr[i].toLowerCase().equals("worked")|| arr[i].toLowerCase().equals("working")|| arr[i].toLowerCase().equals("duration")|| arr[i].toLowerCase().equals("current"))
    		{
    			if(arr[i].toLowerCase().equals("working"))
    				System.out.println("Current employer:");
    		  /*for(int j=0;j<arr.length;j++)   		
    			if(arr[j].toLowerCase().equals("years") || arr[j].toLowerCase().equals("year")|| arr[j].toLowerCase().equals("yrs")|| arr[j].toLowerCase().equals("yr"))
    			*/
    			  return true;
    		} 
    			
    	}
    	return false;
    }
}