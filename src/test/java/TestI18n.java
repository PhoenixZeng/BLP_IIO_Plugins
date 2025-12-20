import com.hiveworkshop.lang.LocalizedFormatedString;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * @author : Phoenix
 * @version : 2021/07/08 13:42
 */
public class TestI18n
{
	@Test
	public void a()
	{
		LocalizedFormatedString string = new LocalizedFormatedString(
				"com.hiveworkshop.text.blp", "BadContent", 128,
				2);
		System.out.println(new String(string.toString().getBytes(Charset.forName("ISO8859-1"))));
	
	}
}
