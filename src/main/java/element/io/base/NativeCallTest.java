package element.io.base;


import cn.hutool.core.io.resource.ClassPathResource;
import org.junit.Before;
import org.junit.Test;

/**
 * @author 张晓华
 * @date 2023-3-23
 */
public class NativeCallTest {

	@Before
	public void loadLibrary() {
		// 加载库文件
		ClassPathResource resource = new ClassPathResource("lib/jni.dll");
		System.load(resource.getAbsolutePath());
	}



	@Test
	public void callTest() {
		NativeFunction nativeFunction = new NativeFunction();
		// 调用native函数获取返回值
		String date = nativeFunction.date();
		System.out.println("native函数的返回值" + date);
	}



}
