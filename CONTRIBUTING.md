## Introduction

This project produces the following `system tray icons` for `Microsoft Windows`:
- `stardatesystemtrayicon`
- `worldtimesystemtrayicon`

where each system tray icon is built upon `basesystemtrayicon`.

In addition, the library `stardate` is produced, which is used by
`stardatesystemtrayicon`, but may also be called through its API by any
third-party `Java` application.

---

## rt.jar

Compilation is set at `Java 8` and so requires the corresponding `rt.jar`:
1. Go to [https://archive.debian.org/debian/pool/main/o/openjdk-8](https://archive.debian.org/debian/pool/main/o/openjdk-8)
1. Download `openjdk-8-jre-headless_8u252-b09-1~deb9u1_arm64.deb`
1. Create the directory `lib` at the project root.
1. Extract `rt.jar` and place into the directory `lib`.

---

## Joda-Time

Compiling `stardatesystemtrayicon` requires `Joda-Time`.  Download the latest `joda-time-x.y.z.jar` from [https://www.joda.org/joda-time](https://www.joda.org/joda-time) and place, as is, into the directory `stardatesystemtrayicon\lib`.

In `build.xml` ensure the version number of the property `jar.joda` matches
that of the jar.

---

## Compile and Jar

Calling `ant` without parameters compiles **ALL** the source code and produces
three jars in `release`:
- `stardate`
- `stardatesystemtrayicon`
- `worldtimesystemtrayicon`

---

## Run a System Tray Icon from a Jar

For `Microsoft Windows`, run `stardatesystemtrayicon` from a command prompt:

    java -jar release/stardatesystemtrayicon-6.0.0.jar

which will launch a tray icon in the system tray.

For `Linux` (and presumably `Apple`), the system tray is unsupported, so instead
from a terminal:

    java -DrunWithoutSystemTrayIcon=true -jar release/stardatesystemtrayicon-6.0.0.jar

which will create a frame/window and on a mouse right-click, will display the
popup menu.

Repeat above for `worldtimesystemtrayicon`.

To print (some of the) system properties, include `-Ddebug=true` (on any operating system).

---

## Create Installer for each System Tray Icon

`NSIS` is used to create the installer (and launcher) for the system tray icons.
In a command prompt or terminal:

    ant Release

produces (with version numbers)
- `StardateSystemTrayIconSetup.exe`
- `WorldTimeSystemTrayIconSetup.exe`

Then install on `Microsoft Windows` and run as any regular application.

---

## Create Javadoc for Stardate

To create the `Javadoc` for the `Stardate` API, in a command prompt or
terminal:

    ant Javadoc

`Javadoc` will also be created when running the `Release` task.

---

## Release

To build the packages for a release on `GitHub`, in a command prompt or terminal:

    ant Release

which produces (with version numbers)
- `StardateSystemTrayIconSetup.exe`
- `WorldTimeSystemTrayIconSetup.exe`
- `stardate.jar`
- `stardate-javadoc.zip`

On `GitHub` create a new `Release` with corresponding version tag and upload the above files.

---

## SpotBugs

Prerequisite: `SpotBugs` must be downloaded and extracted into a directory
`spotbugs` at the project root (remove any release numbering).

Calling
    
    ant Spotbugs
    
will run `SpotBugs` and produce the report `spotbugs.html`.

---

## Migraton from Sourceforge

Documentation of the process used to bring the legacy projects
`stardatesystray` and `wrldtimesystray` from `Sourceforge` into a single
repository on `GitHub`.

Obtain each `SVN` repository from `Sourceforge` and create a working copy:

  ```
    rsync -av svn.code.sf.net::p/stardatesystray/code \
      ./stardatesystraysvn

    svn checkout file://$(pwd)/stardatesystraysvn/code \
      stardatesystraysvnworkingcopy

    rsync -av svn.code.sf.net::p/wrldtimesystray/code \
      ./wrldtimesystraysvn

    svn checkout file://$(pwd)/wrldtimesystraysvn/code \
      wrldtimesystraysvnworkingcopy
  ```

If a dump file is ever required:

  ```
    svnadmin dump --incremental --deltas \
      stardatesystraysvn/code > stardatesystray.dump

    svnadmin dump --incremental --deltas \
      wrldtimesystraysvn/code > wrldtimesystray.dump
  ```

Verify list of authors (should only be `bernmeister =`):

  ```
    cd stardatesystraysvnworkingcopy
    svn log --xml --quiet | grep author | sort -u | perl -pe 's/.*>(.*?)<.*/$1 = /'
    cd ..

    cd wrldtimesystraysvnworkingcopy
    svn log --xml --quiet | grep author | sort -u | perl -pe 's/.*>(.*?)<.*/$1 = /'
    cd ..
  ```

Create `users.txt` to match the list of authors:

  ```
    echo "bernmeister = Bernard Giannetti <thebernmeister@hotmail.com>" > users.txt
  ```

Convert each `SVN` repository to a `Git` repository:

  ```
    git svn clone file://$(pwd)/stardatesystraysvn/code \
      --authors-file=users.txt \
      --no-metadata \
      --prefix "" \
      -s stardatesystraygit

    git svn clone file://$(pwd)/wrldtimesystraysvn/code \
      --authors-file=users.txt \
      --no-metadata \
      --prefix "" \
      -s wrldtimesystraygit
  ```

Clone the main repository from `GitHub`:

  ```
    git clone https://github.com/iconindicators/systemtrayicons
  ```

Insert the two converted `Git` repositories from above to the clone (in a
directory called `sourceforge`) and push back up to `GitHub`:

  ```
    cd systemtrayicons

    git remote add stardatesystraygit $(pwd)/../stardatesystraygit/.git
    git subtree add -P sourceforge/stardatesystray stardatesystraygit HEAD

    git remote add wrldtimesystraygit $(pwd)/../wrldtimesystraygit/.git
    git subtree add -P sourceforge/wrldtimesystray wrldtimesystraygit HEAD

    git push origin main
  ```

References
- [https://sourceforge.net/p/forge/documentation/svn](https://sourceforge.net/p/forge/documentation/svn)
- [https://git-scm.com/book/ms/v2/Git-and-Other-Systems-Migrating-to-Git](https://git-scm.com/book/ms/v2/Git-and-Other-Systems-Migrating-to-Git)

---

## Useful Links

- [https://markdownlivepreview.com](https://markdownlivepreview.com)
- [https://dillinger.io](https://dillinger.io)
