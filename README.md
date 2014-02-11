What is Frames-OrientDB Plugin ?
============

**Frames-OrientDB plugin** is a Java O/G mapper for the [OrientDB](http://www.orientdb.org/) with  the [Play! framework](http://www.playframework.org/) 2. It is used with the [TinkerPop Frames](https://github.com/tinkerpop/frames/wiki) for O/G mapping.

Features
======

* Object-Graph-Mapping by the [TinkerPop Frames](https://github.com/tinkerpop/frames/wiki)

* Support for both the embedded OrientDB and remote OrientDB

* Auto-reloading support

* Custom vertex and edge types support [(sample)](samples/frames-simple-app-with-custom-types)
   * Note that Custom types, Constraints and Indexes are not yet implemented.
    
* Support to enable to customize the format of Model Id ([src](app/com/wingnest/play2/frames/plugin/orientdb/CustomIdManager.java))

* On only embedded OrienDB:

   * Support to enable to use the [OrientDB Studio](https://github.com/nuvolabase/orientdb/wiki/OrientDB-Studio)

Requirements
=========

* Java 5 or 6, 7
* Play 2.2.1

Dependencies
============

* OrientDB 1.5.1

Installing
==========

  1)  Install Play framework 2.2

  2)  Executing the command for installing the [TinkerPop Frames Module](http://goo.gl/0g43T) 

         % git clone git@github.com:sgougi/play21-frames-module.git
         % cd play21-frames-module
         % git checkout 2.4.3
  
  3)  Publishing the Frames Module to your local repository
           
         % cd play21-frames-module
         % play publish-local

  4)  Executing the command for installing the Frames-OrientDB Plugin

         % git clone git@github.com:sgougi/play21-frames-orientdb-plugin.git
         
  5)  Publishing the Frames-OrientDB Plugin to your local repository

         % cd play21-frames-orientdb-plugin
         % play publish-local


Running sample application
=======================

At a command prompt, type the following commands:

         % cd play21-frames-orientdb-plugin
         % cd samples
         % cd frames-simple-app
         % play run

There are basic usage in the source code of a [sample application](samples). 

* [Annotated model classes](samples/frames-simple-app/app/models)
* [Creating a key index](samples/frames-simple-app/app/Global.java)
* [Application configuration: conf/application.conf](samples/frames-simple-app/conf/application.conf)
* [Dependency settings: project/Build.scala](samples/frames-simple-app/project/Build.scala)  
* [Controller with transaction](samples/frames-simple-app/app/controllers/Application.java)

## Facade Classes for GraphDB

The com.wingnest.play2.frames.[GraphDB](https://github.com/sgougi/play21-frames-module/blob/master/app/com/wingnest/play2/frames/GraphDB.java) class is a Facade class.

* GraphDB.commit()
* GraphDB.rollback()
* GraphDB.getGraph()
* GraphDB.createFramedGraph()
* GraphDB.getGraphManager()
* GraphDB.createKeyIndex()
* GraphDB.dropKeyIndex()
* GraphDB.createIndex()
* GraphDB.dropIndex()
* GraphDB.getIndexedKeys()
* GraphDB.getIndex()
* GraphDB.getIndices()

The com.wingnest.play2.frames.[OrientDB] (app/com/wingnest/play2/frames/OrientDB.java) class is a Facade class.

* OrientDB.fromCustomIdToORID()
* OrientDB.fromORIDToCustomId()
* OrientDB.setCustomIdHandler()

## Extended Annotations 

### For Models
####[@Id](https://github.com/sgougi/play21-frames-module/blob/master/app/com/wingnest/play2/frames/annotations/Id.java)
Defines one attribute as id attribute.

 ex:

       public interface A {
         ...
         @Id
         public Object getId();
         ...
      }
      
####[@CustomId](app/com/wingnest/play2/frames/annotations/CustomId.java)
Defines one attribute as custom id attribute. (ref:([CustomIdManager](app/com/wingnest/play2/frames/plugin/orientdb/CustomIdManager.java)))

 ex:

       public interface A {
         ...
         @CustomId
         public Object getCustomId();
         ...
      }

####[@IndexedProperty](https://github.com/sgougi/play21-frames-module/blob/master/app/com/wingnest/play2/frames/annotations/IndexedProperty.java)
Defines one or more attributes as indexed attribute.

 ex:

       public interface A {
         ...
         @IndexedProperty("type")
          public String getType();
         @IndexedProperty("type")
          public void setType(String type);
         ...
       }

       Index<Vertex> index = GraphDB.getIndex(A.class.getSimpleName(), Vertex.class);
       Iterable<Vertex> types = index.get("type", "text");

### For Controllers
####[@WithGraphDB](https://github.com/sgougi/play21-frames-module/blob/master/app/com/wingnest/play2/frames/annotations/WithGraphDB.java)
The **@WithGraphDB** annotation enables annotated Actions and/or Controllers to use **OrientDB** implicitly.

Known Issues
=============
* Nothing

Licence
========
Frames-OrientDB Plugin is distributed under the [Apache 2 licence](http://www.apache.org/licenses/LICENSE-2.0.html).
