package cc.alessandro.jcoderwall.console;


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
public class ListCommand
  implements Command {

  private static final String NAMESPACE = "jcoderwall-cli";

  private static final String CMD_NAME = "list";
  private static final String KEY_ARGS_UNAME = "user";

  private Command.Descriptor descriptor;


  /**
   * {@inheritDoc}
   */
  @Override
  public Command.Descriptor getDescriptor() {
    return (descriptor != null)? this.descriptor:
           (this.descriptor = new Command.Descriptor() {

        @Override
        public String getNamespace() {
          return NAMESPACE;
        }

        @Override
        public String getName() {
          return CMD_NAME;
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
            args.put(KEY_ARGS_UNAME + ":<UserName>",
                     "Username for connect");
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

    final Map<String, Object> argsMap =
      (Map<String, Object>) context.getValue(Context.KEY_COMMAND_LINE_ARGS);

    final IOConsole console = context.getIoConsole();

    if (argsMap != null && argsMap.get(KEY_ARGS_UNAME) != null) {
      try {
        Coder coder =
          new CoderwallClient((String) argsMap.get(KEY_ARGS_UNAME)).get();

        //print
        console.writeOutput(String.format("User: %s Location: %s",
                                          coder.getUsername(),
                                          coder.getLocation()));
        console.writeOutput("Achievements:");
        for (Badge badge: coder.getBadges()) {
          console.writeOutput(String.format("- %s: %s", badge.getName(),
                                            badge.getDescription()));
        }
      }
      catch (IOException exception) {
        throw new ShellException(String.format("Unable to list user value for [%s]: %s",
                                               argsMap.get(KEY_ARGS_UNAME),
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
