import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MyServer
{
	public static void main(String[] args)
	{
		try
		{
			ServerSocket ser=new ServerSocket(4321);
			Socket client=ser.accept();
			
			DataInputStream dis=new DataInputStream(client.getInputStream());
			DataOutputStream dos=new DataOutputStream(client.getOutputStream());
			
			int id=dis.readInt();
			
			Class.forName("com.mysql.jdbc.Driver");
			
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost/gtukb","root","");
			
			Statement st=con.createStatement();
			
			ResultSet rs=st.executeQuery("select * from mytab where sid="+id);
			rs.next(); //or rs.first();
			
			String data=""+rs.getInt(1);
			data=data+" , "+rs.getString(2);
			data=data+" , "+rs.getInt(3);
			
			rs.close();
			st.close();
			con.close();
			
			dos.writeUTF(data);
			
			dis.close();
			dos.close();
			client.close();
			ser.close();
		}
		catch(Exception e)
		{
			System.out.println("\n SERVER ERROR : "+e.getMessage());
		}
	}
}