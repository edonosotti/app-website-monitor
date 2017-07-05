# Changelog

## 2017-07-05

Ported the original Eclipse project to Gradle.

```
ECLIPSE ANDROID PROJECT IMPORT SUMMARY
======================================

Ignored Files:
--------------
The following files were *not* copied into the new Gradle project; you
should evaluate whether these are still needed in your project and if
so manually move them:

* .DS_Store
* .gitignore
* ic_app_icon-web.png
* ic_check_ko-web.png
* ic_check_ok-web.png
* ic_check_problem-web.png
* ic_launcher-web.png
* ic_website-web.png
* license.txt
* proguard-project.txt
* readme.txt

Replaced Jars with Dependencies:
--------------------------------
The importer recognized the following .jar files as third party
libraries and replaced them with Gradle dependencies instead. This has
the advantage that more explicit version information is known, and the
libraries can be updated automatically. However, it is possible that
the .jar file in your project was of an older version than the
dependency we picked, which could render the project not compileable.
You can disable the jar replacement in the import wizard and try again:

android-support-v4.jar => com.android.support:support-v4:22.2.1

Moved Files:
------------
Android Gradle projects use a different directory structure than ADT
Eclipse projects. Here's how the projects were restructured:

* AndroidManifest.xml => app/src/main/AndroidManifest.xml
* assets/ => app/src/main/assets/
* libs/activeandroid-3.0.jar => app/libs/activeandroid-3.0.jar
* lint.xml => app/lint.xml
* res/ => app/src/main/res/
* src/ => app/src/main/java/

Next Steps:
-----------
You can now build the project. The Gradle project needs network
connectivity to download dependencies.

Bugs:
-----
If for some reason your project does not build, and you determine that
it is due to a bug or limitation of the Eclipse to Gradle importer,
please file a bug at http://b.android.com with category
Component-Tools.

(This import summary is for your information only, and can be deleted
after import once you are satisfied with the results.)
```
