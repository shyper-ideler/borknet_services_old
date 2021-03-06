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
import borknet_services.core.*;

/**
 * Class to load configuration files.
 * @author Ozafy - ozafy@borknet.org - http://www.borknet.org
 */
public class Ticket implements Command
{
    /**
     * Constructs a Loader
     * @param debug		If we're running in debug.
     */
	public Ticket()
	{
	}

	public void parse_command(Core C, H Bot, String numeric, String botnum, String username, String params)
	{
		DBControl dbc = Bot.getDBC();
		//check if he's an operator and has a high enough level to kill me
		if(dbc.getAuthLev(username) >1)
		{
			String[] result = params.split("\\s");
			try
			{
				String nick = result[1];
				String chan = result[2];
				if(!result[2].startsWith("#")) throw new ArrayIndexOutOfBoundsException();
				String userinfo[] = dbc.getNickRow(nick);
				if(userinfo[0].equals("0") || userinfo[4].equals("0"))
				{
					C.cmd_notice(numeric, botnum,username, nick + " could not be found, or isn't AUTH'd.");
					return;
				}
				else
				{
					if(dbc.hasTicketPending(userinfo[4], chan))
					{
						C.cmd_notice(numeric, botnum,username, "User already has a ticket pending for "+chan);
						return;
					}
					C.cmd_notice(numeric, botnum,userinfo[0], "You have been issued an invite ticket for channel "+chan+". This ticket is valid for a period of 48hours. You can use my invite command to get to the channel now. Type /msg "+Bot.get_nick()+" invite " + chan);
					dbc.addTicket(userinfo[4],chan,(Long.parseLong(C.get_time())+172800));
					C.cmd_notice(numeric, botnum,username, "Done.");
					return;
				}
			}
			//he didn't, Yoda time!
			catch(Exception e)
			{
				C.cmd_notice(numeric, botnum,username, "/msg " + Bot.get_nick() + " ticket <nick> <#channel>");
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

	public void parse_help(Core C, H Bot, String numeric, String botnum, String username, int lev)
	{
		if(lev > 1)
		{
			C.cmd_notice(numeric, botnum, username, "/msg "+Bot.get_nick()+" ticket <nick> <#channel>");
		}
		else
		{
			C.cmd_notice(numeric, botnum, username, "This command is either unknown, or you need to be opered up to use it.");
			return;
		}
	}
	public void showcommand(Core C, H Bot, String numeric, String botnum, String username, int lev)
	{
		if(lev > 1)
		{
			C.cmd_notice(numeric, botnum, username, "TICKET              Issue a ticket to a user. - level 2");
		}
	}
}