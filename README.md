# FlashMonkey developer interface

![iOOily FlashMonkey logo](./readme_resources/FlashMonkey_logo.png)


This is the repository for the Patent Pending FlashMonkey intelligent study application. FlashMonkey is an interactive 
and multi-media learning application developed by FlashMonkey Inc. 

- Play Nice Rule -
Before contributing or modifying another author's code, please inform them that you are modifying or modified their code. 

<b>Bug Status</b>: Issue #1 when 1st setting up the IDE and trying to Run FlashMonkey. There was an image missing from 
src/image/ ... . The image is in the correct location in the repository. 

<b>Fix</b>: 2 ways. If you haven't made changes to your branch yet, Branch from the latest version of the main branch, 
and in your IDE pull from your branch. Or download the file directly to your src/image directory. The name of the image 
is <i>flash_astronaut_grey_150.png</i>. Note the image is in <i>src/image/emojis/ ...</i>

#Set up this project for Maven and JavaFX
Note that setting up a Java Project for JavaFX after Java 11 is more complex than they were with previous Java SDK's. 
JavaFX is no longer included in the SDK. JavaFX is an open-source project by OpenJFX https://www.openjfx.io

Note: As of April 2021 we are using Java LTS 11 and JavaFX 14. Ask what version of JavafX and Java is being used if this
document is out of date. We are not using Modules, the project is "non-modular".  

Helpful links:
+ Jetbrains blog as of 2021 under the get started with _JavaFX and Maven, non-modular_ https://blog.jetbrains.com/idea/2021/01/intellij-idea-and-javafx/
+ OpenJFX docs _Getting Started with JavaFX_ https://openjfx.io/openjfx-docs/
+ Setting up the project for GitHub using IntelliJ. If this is your first time to set up Git and GitHub, start with the 
following  https://www.jetbrains.com/help/idea/enabling-version-control.html Otherwise, if you are already familiar with
setting up Git and only need to set up the project with GitHub use: https://www.jetbrains.com/help/idea/github.html


1. Set up a new project as a Maven Project.

2. Connect the project to the FlashMonkey Inc Repo in GitHub using the instructions above. See your supervisor for access,
password and username.

3. Import the project

4. Ensure the project is synchronized with Maven. Note that the local Maven repo is stored on your machine in the _".m2"_
directory. The preceeding "." indicates it is a hidden directory and you will need to ensure you can access and see 
hidden directories on your machine. Usually the _".m2"_ directory is in your user root directory. In my case, on a windows
machine, it is in the _"C://users/me/.m2"_ 

5. If the project is set up correctly. You should be able to run the project using the green right triangle on the top 
right of the IntelliJ IDE. 

6. For more advanced settings, and before uploading your additions to the FlashMonkey GitHub private Repo, ensure that 
the project will compile and run using maven. This can be done with the Maven slideout on the far right side of the IDE.
under -> Lifecycle -> clean, then -> Lifecycle -> package. You may also use -> Lifecycle -> install for this project. We
do not upload Proprietary Libraries to Maven.  


**JavaDocs** To generate JavaDocs using IntelliJ. Goto Tools -> Generate JavaDoc. Recommend generating javadocs to a 
file outside of the project. Else IntelliJ may intelligently decide to stick JavaDocs where it is most convienient. 
Select Private and press "ok". A browser page should be displayed that shows the documents. 


**To get started and IDE setup**
_If you have Gradle installed:_ This is a Gradle project. The build.gradle file is under flashmonkey.multimedia.

_Download the video "GetStarted.mp4" to see where the flashMonkeyFile directory files are. This is where flashcard 
decks are stored. You may need to pause the video to see it. Download the flashMonkeyFile located above, and place it 
your IdeaProjects folder. The file contains a card deck which "may?" be the issue with attempting to get FlashMonkey 
running the first time._  

<pre>
   See wiki under <a href = "https://github.com/runnermann/FlashMonkey-MultiMedia/wiki/Downloading-to-your-IDE,-and-finding-the-important-files."/>Downloading to your IDE, and finding the important files</a>

</pre>



**To Modify Shapes used in drawTools:** 
<pre>
   see wiki under <a href = "https://github.com/runnermann/FlashMonkey-MultiMedia/wiki/Adding-shapes"/>Adding Modifying Shapes</a>

</pre>

**To add a new test card or modify a stub Test card.** 
<pre>
   see wiki under <a href = "https://github.com/runnermann/FlashMonkey-MultiMedia/wiki/Adding-Test-Cards"/>Adding Test Cards </a>
   
</pre>

**Resources for learning about some of the API's use in FlashMonkey.**
<pre>
   See wiki under <a href = "https://github.com/runnermann/FlashMonkey-MultiMedia/wiki/Learning-Resources"/>Learning resouces </a>

</pre>   


**_This is a Fair Developer and Fair Use License:_**

**Copyright (c) 2019 - 2021 FlashMonkey Inc. & Lowell Stadelman**

_The intent of the Fair Developer and Fair Use License is to give authors recognition for their contributions and allow early open development with a stipulation that an author is recognized and compensated for their works. And that a class created by an author is generally copywrite protected under this license. This license futher specifies conditions for its future distribution, if it is distributed, by rewarding authors for their contributions first._ 

By contributing to this project, if you contribute code you are an author and contributor.

By contributing to this project, if you contribute code you are an author and contributor.

1. By participating in this project you are not granted any right to redistribute code that you did not author or create. You recognize the works of other authors as belonging to those authors and will not distribute their material without their written permission.

2. By recognizing the works of other authors, contributors will not duplicate or rewrite other authors accepted works, nor copy or recreate the intent of their accepted works.

3. By contributing or modifying other author's works, your contributions are done in kind.  

4. Contributors recognize that their contributions may or may not be included in a distribution and make no claim to works that they did not contribute towards nor claim rights to software that is distributed if their works are not included.  

5. If your contribution is included in a distribution, contributors recognize that their reimbursement is not more than the share of work in the overall contribution of the project by lines of quality code.
    
6. By contributing to this project you make no claim to iOOily's contributions and it's intent or patentable materials if any.  

7. No contributions shall be allowed for malicious intent.  

8. You grant any contributor the right to download and use your contributions for the purpose of development for this project, so that they may make contributions and incorporate your contributions as a part of their contributions. As such, their contributions may rely on your contributions and you agree that you make no claim beyond your contribution.  

9. Contributors grant iOOily(TM) the right to redistribute their contributions for any purpose, and make no demand for reimbursement if it is distributed on a free basis.  

10. All original works by authors shall be maintained and marked by the author. If it is not marked, contributors may not claim a work they did not contribute towards.  

11. Improvements to a contribution may or may not be accepted and the author of the improvement may or may not be compensated. If the original works interfere with the performance of another author's works it may or may not be accepted or compensated. No contributor may interfere or improve another contributors works in order to dilute another author's contributions or performance.  

12. iOOily(TM) is the maintainer of FlashMonkey and its organization management, and ownership makes final decisions on contributions, distribution, and the direction of FlashMonkey. If an author or organization other than iOOily desires to distribute FlashMonkey, iOOily may or may not grant distribution rights. If rights are granted, all contributors and distributors agree that compensation is first given to the authors and contributors of FlashMonkey.  

13. Quality performance over incentives by profits. This code shall not be distributed by any organization that is traded in any public exchange. Meaning but not limited to exchanges such as publicly traded stock-markets like the NYSE. This includes organizations and investments with the intent of selling shares such as public IPO offerings. FlashMonkey is to assist with education and where gains of profit shall not be the highest priority.


14. DISCLAIMER OF WARRANTY.  

COVERED CODE IS PROVIDED UNDER THIS LICENSE ON AN "AS IS" BASIS, WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING, WITHOUT LIMITATION, WARRANTIES THAT THE COVERED CODE IS FREE OF DEFECTS, MERCHANTABLE, FIT FOR A PARTICULAR PURPOSE OR NON-INFRINGING. THE ENTIRE RISK AS TO THE QUALITY AND PERFORMANCE OF THE COVERED CODE IS WITH YOU. SHOULD ANY COVERED CODE PROVE DEFECTIVE IN ANY RESPECT, YOU (NOT THE INITIAL DEVELOPER OR ANY OTHER CONTRIBUTOR) ASSUME THE COST OF ANY NECESSARY SERVICING, REPAIR OR CORRECTION. THIS DISCLAIMER OF WARRANTY CONSTITUTES AN ESSENTIAL PART OF THIS LICENSE. NO USE OF ANY COVERED CODE IS AUTHORIZED HEREUNDER EXCEPT UNDER THIS DISCLAIMER.
