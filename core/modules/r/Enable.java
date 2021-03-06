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
public class Enable implements Command
{
    /**
     * Constructs a Loader
     * @param debug		If we're running in debug.
     */
	public Enable()
	{
	}

	public void parse_command(Core C, R Bot, String numeric, String botnum, String username, String params)
	{
		try
		{
			String result[] = params.split("\\s");
			int enable = Integer.parseInt(result[1]);
			CoreDBControl dbc = C.get_dbc();
			String user[] = dbc.getUserRow(username);
			if(user[5].equals("1"))
			{
				switch(enable) {
					case 0:
						Bot.enable(false);
						C.cmd_notice(numeric, botnum, username, "Bot requests are now disabled");
						C.report(user[1] + " has disabled " + Bot.get_nick());
						break;
					case 1:
						Bot.enable(true);
						C.cmd_notice(numeric, botnum, username, "Bot requests are now enabled");
						C.report(user[1] + " has enabled " + Bot.get_nick());
						break;
					default:
						C.cmd_notice(numeric, botnum, username, "/msg "+Bot.get_nick()+" enable <0|1>");
						break;
				}
				return;
			}
			else
			{
				C.cmd_notice(numeric, botnum, username, "This command is either unknown, or you need to be opered up to use it.");
				return;
			}
		}
		catch(Exception e)
		{
			C.cmd_notice(numeric, botnum, username, "/msg "+Bot.get_nick()+" enable <0|1>");
		}
	}

	public void parse_help(Core C, R Bot, String numeric, String botnum, String username, int lev)
	{
		if(lev > 99)
		{
			C.cmd_notice(numeric, botnum, username, "/msg "+Bot.get_nick()+" enable <0|1>");
		}
	}
	public void showcommand(Core C, R Bot, String numeric, String botnum, String username, int lev)
	{
		if(lev > 99)
		{
			C.cmd_notice(numeric, botnum, username, "ENABLE              Enable the request system. - level 100");
		}
	}
}