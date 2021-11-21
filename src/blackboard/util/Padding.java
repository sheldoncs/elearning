package blackboard.util;

public class Padding {

	public String lPad(String MyValue, String MyPadCharacter, int MyPaddedLength)
    {
        String padString = "";
        int padLength = 0;

        padLength = MyPaddedLength - MyValue.length();
        for (int i = 0; i < padLength; i++)
        {
            padString = padString + MyPadCharacter;
        }
        padString = padString + MyValue;

        return padString;
    }
}
