/**
 * Copyright (c) 2012 Alessandro Leite, http://alessandro.cc <alessandro.leite@alessandro.cc>
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cc.alessandro.coderwall.clamshell.command;


import alessandro.cc.jcoderwall.Badge;
import alessandro.cc.jcoderwall.Coder;
import alessandro.cc.jcoderwall.CoderwallClient;

import java.io.IOException;

import java.util.LinkedHashMap;
import java.util.Map;

import org.clamshellcli.api.Command;
import org.clamshellcli.api.Context;
import org.clamshellcli.api.IOConsole;
import org.clamshellcli.core.ShellException;


/**
 * This Command lists the Achievements of an Coderwall user.
 * <p>
 * The List command format is:
 *  <pre>
 *    list [user:&lt;UserName&gt;]"
 *  </pre>
 * </p>
 * @author Alessandro Leite
 */
public class CoderwallCommand
  implements Command {

  private static final String NAMESPACE = "syscmd";
  private static final String ACTION_NAME = "coderwall";
  private static final String KEY_ARGS_UNAME = "user";

  /**
   * {@inheritDoc}
   */
  @Override
  public Command.Descriptor getDescriptor() {
    return (new Command.Descriptor() {

      @Override
      public String getNamespace() {
        return NAMESPACE;
      }

      @Override
      public String getName() {
        return ACTION_NAME;
      }

      @Override
      public String getDescription() {
        return "List the achievements of an User";
      }

      @Override
      public String getUsage() {
        return "list [user:<UserName>]";
      }

      Map<String, String> args;

      @Override
      public Map<String, String> getArguments() {
        if (args == null) {
          args = new LinkedHashMap<String, String>();

          args.put(KEY_ARGS_UNAME + ":<UserName>", "Username for connect");
        }
        return args;
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object execute(Context context) {
    final IOConsole console = context.getIoConsole();

    final String[] args =
      (String[]) context.getValue(Context.KEY_COMMAND_LINE_ARGS);

    if (args != null && args.length > 0) {

      try {
        Coder coder = new CoderwallClient(args[0]).get();

        //print
        console.writeOutput(String.format("User: %s Location: %s \n",
                                          coder.getUsername(),
                                          coder.getLocation()));
        console.writeOutput("Achievements: \n");
        for (Badge badge: coder.getBadges()) {
          console.writeOutput(String.format("- %s: %s \n", badge.getName(),
                                            badge.getDescription()));
        }
      }
      catch (IOException exception) {
        throw new ShellException(String.format("Unable to list user value for [%s]: %s",
                                               args[0],
                                               exception.getMessage()),
                                 exception);
      }
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void plug(Context context) {
  }
}