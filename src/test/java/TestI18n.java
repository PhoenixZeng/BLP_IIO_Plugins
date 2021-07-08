import com.hiveworkshop.lang.LocalizedFormatedString;
import org.junit.Test;

/**
 * @author : Phoenix
 * @version : 2021/07/08 13:42
 */
public class TestI18n
{
	@Test
	public void a()
	{
		System.out.println(new LocalizedFormatedString(
				"com.hiveworkshop.text.blp", "BadContent", 128,
				2));
	
	}
}
