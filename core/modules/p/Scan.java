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
import java.io.*;
import java.util.*;
import java.text.*;
import borknet_services.core.*;

/**
 * Class to load configuration files.
 * @author Ozafy - ozafy@borknet.org - http://www.borknet.org
 */
public class Scan implements Command
{
    /**
     * Constructs a Loader
     * @param debug		If we're running in debug.
     */
	public Scan()
	{
	}

 public void parse_command(Core C, P Bot, String numeric, String botnum, String username, String params)
	{
  CoreDBControl dbc = C.get_dbc();
		String user[] = dbc.getUserRow(username);
		if(user[4].equals("0"))
		{
			C.cmd_notice(numeric, botnum,username, "You are not AUTH'd.");
			return;
		}
		String auth[] = dbc.getAuthRow(user[4]);
		//check if he's an operator and has a high enough level to kill me
		if(user[5].equals("1") && Integer.parseInt(auth[3]) >949)
		{
			String[] result = params.split("\\s");
			try
			{
				//get the stuff
				String channel = result[1];
				//we got a channel
				if(!channel.startsWith("#")) throw new ArrayIndexOutOfBoundsException();
    String[] users = dbc.getChannelUsers(channel);
    if(!users[0].equals("0"))
    {
     for(String un : users)
     {
      User u = dbc.getUser(un);
      if(u instanceof User)
      {
       Bot.scan(u.getNumeric(),u.getIp(),u.getHost());
      }
     }
     C.cmd_notice(numeric,botnum,username, "Scan has been started.");
    }
    else
    {
     C.cmd_notice(numeric,botnum,username, "Can't find that channel!");
    }
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				C.cmd_notice(numeric, botnum,username , "/msg " + Bot.get_nick() + " scan <#channel>");
				return;
			}
		}
		//user doesn't have access, that bastard!
		else
		{
			C.cmd_notice(numeric, botnum,username, "This command is either unknown, or you need to be opered up to use it.");
			return;
		}
	}

	public void parse_help(Core C, P Bot, String numeric, String botnum, String username, int lev)
	{
		if(lev > 949)
		{
			C.cmd_notice(numeric, botnum, username, "/msg " + Bot.get_nick() + " scan <#channel>");
		}
		else
		{
			C.cmd_notice(numeric, botnum, username, "This command is either unknown, or you need to be opered up to use it.");
		}
	}
	public void showcommand(Core C, P Bot, String numeric, String botnum, String username, int lev)
	{
		if(lev > 949)
		{
			C.cmd_notice(numeric, botnum, username, "SCAN                Scan a channel for proxies. - level 950.");
		}
	}
}