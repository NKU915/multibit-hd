package org.multibit.hd.ui.fest.use_cases.send_request;

import org.fest.swing.fixture.FrameFixture;
import org.multibit.hd.ui.fest.use_cases.AbstractFestUseCase;
import org.multibit.hd.ui.languages.MessageKey;
import org.multibit.hd.ui.views.themes.Themes;

import java.util.Map;

/**
 * <p>Use case to provide the following to FEST testing:</p>
 * <ul>
 * <li>Verify the "contacts" screen add Alice contact</li>
 * </ul>
 * <p>Requires the "contacts" screen to be showing</p>
 *
 * @since 0.0.1
 *  
 */
public class SendNoFundsContactUseCase extends AbstractFestUseCase {

  public SendNoFundsContactUseCase(FrameFixture window) {
    super(window);
  }

  @Override
  public void execute(Map<String, Object> parameters) {

    // Click on Send
    window
      .button(MessageKey.SEND.getKey())
      .click();

    // Verify the wizard appears
    window
      .label(MessageKey.SEND_BITCOIN_TITLE.getKey());

    // Verify buttons
    window
      .button(MessageKey.NEXT.getKey())
      .requireVisible()
      .requireDisabled();

    window
      .button(MessageKey.CANCEL.getKey())
      .requireVisible()
      .requireEnabled();

    // Use a public domain standard address
    verifyBitcoinAddressField("", false);
    verifyBitcoinAddressField(" ", false);
    verifyBitcoinAddressField("AhN", false);
    verifyBitcoinAddressField("AhN6rPdrMuKBGFDKR1k9A8SCLYaNgXhty", false);
    verifyBitcoinAddressField("1AhN6rPdrMuKBGFDKR1k9A8SCLYaNgXht", false);
    verifyBitcoinAddressField("1AhN6rPdrMuKBGFDKR1k9A8SCLYa", false);
    verifyBitcoinAddressField("1AhN6rPdrMuKBGFDk9A8SCLYaNgXhty", false);

    // Use a public domain P2SH address
    verifyBitcoinAddressField("35b9vsyH1KoFT5a5KtrKusaCcPLkiSo1tU", true);
    verifyBitcoinAddressField("35b9vsyH1KoFT5a5KtrKusaCcPLkiSo1t", false);

    // Set it to the MultiBit address
    verifyBitcoinAddressField("1AhN6rPdrMuKBGFDKR1k9A8SCLYaNgXhty", true);

    // TODO Verify cancelling

    window
      .button(MessageKey.NEXT.getKey())
      .requireVisible()
      .requireDisabled();

    verifyBitcoinAmountField("", false);
    verifyBitcoinAmountField(" ", false);
    verifyBitcoinAmountField("abc", false);
    verifyBitcoinAmountField("'", false);

    verifyBitcoinAmountField("0", true);
    verifyBitcoinAmountField("0.0", true);

    verifyBitcoinAmountField("-1", false);
    verifyBitcoinAmountField("-0.1", false);
    verifyBitcoinAmountField("-20,000,000,000.12345", false);

    verifyBitcoinAmountField("0.1", true);
    verifyBitcoinAmountField("0.00001", true); // 1 sat in mBTC

    verifyBitcoinAmountField("1", true); // 1 BTC
    verifyBitcoinAmountField("10", true);
    verifyBitcoinAmountField("100", true);
    verifyBitcoinAmountField("1000", true); // 1 BTC

    verifyBitcoinAmountField("1,000", true);

    verifyBitcoinAmountField("1 000", false); // 1,000 BTC
    verifyBitcoinAmountField("1 000 000 000", false); // 1,000,000 BTC

    verifyBitcoinAmountField("21000000000", true); // 21,000,000 BTC
    verifyBitcoinAmountField("20000000000.12345", true); // 20,000,000,000.12345 mBTC

    verifyBitcoinAmountField("21,000,000,000", true); // 21,000,000 BTC
    verifyBitcoinAmountField("20,000,000,000.12345", true); // 20,000,000,000.12345 mBTC

    // Cancel from wizard

    window
      .button(MessageKey.CANCEL.getKey())
      .click();

    // Verify underlying detail screen
    window
      .button(MessageKey.SEND.getKey())
      .requireVisible()
      .requireEnabled();

  }

  /**
   * Verifies that an incorrect Bitcoin amount format is detected on focus loss
   *
   * @param text    The text to use as a Bitcoin address
   * @param isValid True if the validation should pass
   */
  private void verifyBitcoinAmountField(String text, boolean isValid) {

    // Set the text directly on the combo box editor
    window
      .textBox(MessageKey.BITCOIN_AMOUNT.getKey())
      .setText(text);

    // Lose focus to trigger validation
    window
      .button(MessageKey.PASTE.getKey())
      .focus();

    // Verify the focus change and background color of the editor
    if (isValid) {
      window
        .textBox(MessageKey.BITCOIN_AMOUNT.getKey())
        .background()
        .requireEqualTo(Themes.currentTheme.dataEntryBackground());
    } else {
      window
        .textBox(MessageKey.BITCOIN_AMOUNT.getKey())
        .background()
        .requireEqualTo(Themes.currentTheme.invalidDataEntryBackground());
    }

  }

  /**
   * Verifies that an incorrect Bitcoin format is detected on focus loss
   *
   * @param text    The text to use as a Bitcoin address
   * @param isValid True if the validation should pass
   */
  private void verifyBitcoinAddressField(String text, boolean isValid) {

    // Set the text directly on the combo box editor
    window
      .textBox(MessageKey.RECIPIENT.getKey())
      .setText(text);

    // Lose focus to trigger validation
    window
      .button(MessageKey.PASTE.getKey())
      .focus();

    // Verify the focus change and background color of the editor
    if (isValid) {
      window
        .textBox(MessageKey.RECIPIENT.getKey())
        .background()
        .requireEqualTo(Themes.currentTheme.dataEntryBackground());
    } else {
      window
        .textBox(MessageKey.RECIPIENT.getKey())
        .background()
        .requireEqualTo(Themes.currentTheme.invalidDataEntryBackground());
    }

  }

  /**
   * Verifies that clicking cancel with data present gives a Yes/No popover
   */
  private void verifyCancel() {

    // Click Cancel
    window
      .button(MessageKey.CANCEL.getKey())
      .click();

    // Expect Yes/No popup)
    window
      .button(MessageKey.YES.getKey())
      .requireVisible()
      .requireEnabled();

    window
      .button(MessageKey.CLOSE.getKey())
      .requireVisible()
      .requireEnabled();

    // Click No
    window
      .button(MessageKey.NO.getKey())
      .requireVisible()
      .requireEnabled()
      .click();
  }

}
