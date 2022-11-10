package net.lax1dude.eaglercraft.sp;

public class VFSTestClass {
	
	public static void test(VirtualFilesystem vfs) {
		/*
		System.out.println("'test1' exists: " + vfs.getFile("test1").exists());
		System.out.println("'test1' chars: " + vfs.getFile("test1").getAllChars());
		System.out.println("'test2' chars: " + vfs.getFile("test2").getAllChars());
		System.out.println("'test3' chars: " + vfs.getFile("test3").getAllChars());
		System.out.println("'test2' exists: " + vfs.getFile("test2").exists());
		System.out.println("'test3' exists: " + vfs.getFile("test3").exists());

		System.out.println("'test1' set chars 'test string 1': " + vfs.getFile("test1").setAllChars("test string 1"));
		System.out.println("'test2' set chars 'test string 2': " + vfs.getFile("test2").setAllChars("test string 2"));
		System.out.println("'test3' set chars 'test string 3': " + vfs.getFile("test3").setAllChars("test string 3"));

		System.out.println("'test1' exists: " + vfs.getFile("test1").exists());
		System.out.println("'test2' exists: " + vfs.getFile("test2").exists());
		System.out.println("'test3' exists: " + vfs.getFile("test3").exists());

		System.out.println("'test1' chars: " + vfs.getFile("test1").getAllChars());
		System.out.println("'test2' chars: " + vfs.getFile("test2").getAllChars());
		System.out.println("'test3' chars: " + vfs.getFile("test3").getAllChars());
		
		System.out.println("'test3' delete: " + vfs.getFile("test3").delete());
		System.out.println("'test3' exists: " + vfs.getFile("test3").exists());
		
		System.out.println("'test2' delete: " + vfs.getFile("test2").delete());
		System.out.println("'test2' chars: " + vfs.getFile("test2").getAllChars());

		System.out.println("'test4' exists: " + vfs.getFile("test4").exists());
		System.out.println("'test1' to 'test4' rename: " + vfs.getFile("test1").rename("test4"));
		System.out.println("'test4' exists: " + vfs.getFile("test4").exists());
		System.out.println("'test4' chars: " + vfs.getFile("test4").getAllChars());
		System.out.println("'test1' exists: " + vfs.getFile("test1").exists());
		System.out.println("'test4' to 'test1' rename: " + vfs.getFile("test4").rename("test1"));
		System.out.println("'test4' exists: " + vfs.getFile("test4").exists());
		System.out.println("'test4' chars: " + vfs.getFile("test4").getAllChars());
		System.out.println("'test1' exists: " + vfs.getFile("test1").exists());
		System.out.println("'test1' chars: " + vfs.getFile("test1").getAllChars());

		System.out.println("'test1' cache get chars: " + vfs.getFile("test1", true).getAllChars());
		System.out.println("'test1' cache exists: " + vfs.getFile("test1", true).exists());
		System.out.println("'test1' cache delete: " + vfs.getFile("test1", true).delete());
		System.out.println("'test1' cache exists: " + vfs.getFile("test1", true).exists());
		System.out.println("'test1' cache get chars: " + vfs.getFile("test1", true).getAllChars());
		
		System.out.println("'test1' cache set chars 'test cache string 1': " + vfs.getFile("test1", true).setAllChars("test cache string 1"));
		System.out.println("'test2' cache set chars 'test cache string 2': " + vfs.getFile("test2", true).setAllChars("test cache string 2"));
		System.out.println("'test3' cache set chars 'test cache string 3': " + vfs.getFile("test3", true).setAllChars("test cache string 3"));
		
		System.out.println("'test1' cache chars: " + vfs.getFile("test1").getAllChars());
		System.out.println("'test2' cache chars: " + vfs.getFile("test2").getAllChars());
		System.out.println("'test3' cache chars: " + vfs.getFile("test3").getAllChars());
		
		System.out.println("'test1' cache copy chars: " + VirtualFilesystem.utf8(vfs.getFile("test1").getAllBytes(true)));
		System.out.println("'test2' cache copy chars: " + VirtualFilesystem.utf8(vfs.getFile("test2").getAllBytes(true)));
		System.out.println("'test3' cache copy chars: " + VirtualFilesystem.utf8(vfs.getFile("test3").getAllBytes(true)));
		*/
		
		VFile f = new VFile("test1");
		System.out.println(f);
		
		f = new VFile("/test1");
		System.out.println(f);
		
		f = new VFile("/test2/");
		System.out.println(f);
		
		f = new VFile("test2/");
		System.out.println(f);
		
		f = new VFile("test2/teste");
		System.out.println(f);
		
		f = new VFile("\\test2\\teste");
		System.out.println(f);
		
		f = new VFile("\\test2\\teste\\..\\eag");
		System.out.println(f);
		
		f = new VFile("test2", "teste", "eag");
		System.out.println(f);
		
		f = new VFile(f, "../", "test2", "teste", "eag");
		System.out.println(f);
		
		f = new VFile(f, "../../", "test2", ".", "eag");
		System.out.println(f);
		
		f = new VFile("you/eag", f);
		System.out.println(f);
		
		f = new VFile(" you/ eag ", f);
		System.out.println(f);
		
		f = new VFile("\\yee\\", f);
		System.out.println(f);
		
		f = new VFile("\\yee\\", "yeeler", f, new VFile("yee"));
		System.out.println(f);
		
		f = new VFile(f, new VFile("yee2"));
		System.out.println(f);
		
		f = new VFile("yee/deevler/", new VFile("yee2"));
		System.out.println(f);
		
		f = new VFile("yee/../../../../", new VFile("yee2"));
		System.out.println(f);
		
		f = new VFile("yee/../../deevler../../", new VFile("yee2"));
		System.out.println(f);
	}
	
}
