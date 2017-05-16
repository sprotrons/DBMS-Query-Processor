//A pairing tool
public class PairingTool<A, B>
{
	//override datatypes
	public A a;
	public B b;

	//constructor
	public PairingTool()
	{
		a = null;
		b = null;
	}
	
	//override constructor
	public PairingTool(A a, B b)
	{         
		this.a= a;
		this.b= b;
	}

	//set data
	public void put(A a1, B b1)
	{
		this.a = a1;
		this.b = b1;
	}

	//get first data
	public A getFirst()
	{
		return a;
	}

	//get second data
	public B getSecond()
	{
		return b;
	}
	
	public void setSecond(B b1)
	{
		b = b1;
	}
	
	//check equal
	public boolean equals(Object obj)
	{
		if(!(obj instanceof PairingTool))
		{
			return false;
		}
		if(obj == this)
		{
			return true;
		}
		if(obj.equals(null))
		{
			return false;
		}
		@SuppressWarnings("unchecked")
		PairingTool<A,	B> p = (PairingTool<A, B>)obj;
		if(p.getFirst() == null || p.getSecond() == null)
		{
			System.out.println("Pair NULL Error!");
			return false;
		}
		if(p.getFirst().equals(a) && p.getSecond().equals(b))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}