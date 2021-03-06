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
# MERCHANTABotILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place - Suite 330, Botoston, MA  02111-1307, USA.
#
*/

/*
The actual module core.
It loads the config.
Creates all needed classes.

It can be used to create both servers & clients.
*/

import java.io.*;
import java.util.*;
import java.sql.*;
import borknet_services.core.*;

public class G implements Modules
{
	private Core C;
	private Server ser;
	private DBControl dbc;
	private GameTimer GT;
	private String description = "";
	private String nick = "";
	private String ident = "";
	private String host = "";
	private String pass = "";
	private String numeric = "";
	private String num = "AAA";
	private String reportchan = "";
	private ArrayList<Object> cmds = new ArrayList<Object>();
	private ArrayList<String> cmdn = new ArrayList<String>();

 private final int TRIVIA = 1;

	public G()
	{
	}

	public void start(Core C)
	{
		this.C = C;
		load_conf();
		numeric = C.get_numeric();
		dbc = new DBControl(C,this);
		ser = new Server(C,dbc,this);
		C.cmd_create_service(num, nick, ident, host, "+ir", description);
		reportchan = C.get_reportchan();
		C.cmd_join(numeric, num, reportchan);
  dbc.loadTriviaQuestions();
		GT = new GameTimer(this,10,TRIVIA);
		Thread th1 = new Thread(GT);
		th1.setDaemon(true);
		th1.start();
	}

	public void setCmnds(ArrayList<Object> cmds,ArrayList<String> cmdn)
	{
		this.cmds = cmds;
		this.cmdn = cmdn;
	}

	public ArrayList<Object> getCmds()
	{
		return cmds;
	}

	public ArrayList<String> getCmdn()
	{
		return cmdn;
	}

	public void stop()
	{
		C.cmd_kill_service(numeric+num, "Quit: Soon will I rest, yes, forever sleep. Earned it I have. Twilight is upon me, soon night must fall.");
	}

	public void hstop()
	{
		C.cmd_kill_service(numeric+num, "Quit: Happens to every guy sometimes this does.");
	}

	private void load_conf()
	{
		try
		{
			ConfLoader loader = new ConfLoader(C,"core/modules/"+this.getClass().getName().toLowerCase()+"/"+this.getClass().getName().toLowerCase()+".conf");
			loader.load();
			Properties dataSrc = loader.getVars();
			//set all the config file vars
			description = dataSrc.getProperty("description");
			nick = dataSrc.getProperty("nick");
			ident = dataSrc.getProperty("ident");
			host = dataSrc.getProperty("host");
			pass = dataSrc.getProperty("pass");
			num = dataSrc.getProperty("numeric");
		}
		catch(Exception e)
		{
			C.printDebug("Error loading configfile.");
			C.debug(e);
			C.die("SQL error, trying to die gracefully.");
		}
	}

	public void parse(String msg)
	{
		try
		{
			ser.parse(msg);
		}
		catch(Exception e)
		{
			C.debug(e);
		}
	}

	public String get_num()
	{
		return numeric;
	}
	public String get_corenum()
	{
		return num;
	}
	public String get_nick()
	{
		return nick;
	}
	public String get_host()
	{
		return host;
	}
	public DBControl get_dbc()
	{
		return dbc;
	}
	public void clean()
	{
  ser.clean();
	}
 
 public void talk(String channel, String s)
 {
  C.cmd_privmsg(numeric, num , channel, s);
 }
 
 public void report(String s)
 {
  talk(reportchan, s);
 }
 
 public void tick(int action)
 {
  switch(action)
  {
   case TRIVIA:
    dbc.tickTriviaGames();
    break;
  }
 }

	public void reop(String chan)
	{
		//gets issued if services got restarted during a split for resync reasons.
	}
}