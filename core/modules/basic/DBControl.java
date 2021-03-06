/**
#
# BorkNet Services Core
#

#
# Copyright (C) 2004 Ozafy - ozafy@borknet.org - http://www.borknet.org
#
# This program is free software; you can redistribute it and/or
# modify it under the terms of the GNU General Public License
# as published by the Free Software Foundation; either version 2
# of the License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
#
*/



/*
This class can be used to communicate with the Core's db,
or if you want your own db connection.

I've included one example method.
*/
import java.sql.*;
import java.util.*;
import java.io.*;
import java.security.*;
import borknet_services.core.*;

/**
 * The database communication class of the BorkNet IRC Core.
 * @author Ozafy - ozafy@borknet.org - http://www.borknet.org
 */
public class DBControl
{
	/** Database server */
	private String server;
	/** Database user */
	private String user;
	/** Database password */
	private String password;
	/** Database */
	private String db;
	/** Database connection */
	private Connection con;
	/** Main bot */
	private Core C;

	private Basic Bot;

	/**
	 * Constructs a Database connection.
	 * @param server		Database server
	 * @param user			Database user
	 * @param pass			Database password
	 * @param db			Database
	 * @param debug			Are we debugging?
	 * @param B				Main bot
	 */
	public DBControl(Core C, Basic Bot, Connection con)
	{
		try
		{
			this.C = C;
			this.Bot = Bot;
			this.con = con;
		}
		catch(Exception e)
		{
			C.printDebug("Database error!");
			C.die("SQL error, trying to die gracefully.");
		}
	}

	/**
	 * Check if a numeric is on a channel
	 * @param user		numeric to check
	 * @param channel	channel to check
	 *
	 * @return			true or false
	 */
	public boolean isOnChan(String user, String channel)
	{
		try
		{
			PreparedStatement pstmt;
			pstmt = con.prepareStatement("SELECT * FROM userchans WHERE BINARY user = ? AND channel = ?");
			pstmt.setString(1,user);
			pstmt.setString(2,channel);
			ResultSet rs = pstmt.executeQuery();
			rs.first();
			String mode = rs.getString("modes");
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
}