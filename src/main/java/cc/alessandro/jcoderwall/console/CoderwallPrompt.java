package cc.alessandro.jcoderwall.console;

import org.clamshellcli.api.Context;
import org.clamshellcli.api.Prompt;

public class CoderwallPrompt implements Prompt{
  
  private static final String PROMPT = "jcoderwall-cli > ";

  @Override
  public String getValue(Context context) {
    return PROMPT;
  }

  @Override
  public void plug(Context context) {
  }
}