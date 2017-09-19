package com.localytics.sbt.dynamodb

import sbt.File
import sbt.Keys._

import scala.sys.process._

object StopDynamoDBLocal {

  def apply(dbPathOpt: Option[String], clean: Boolean, port: Int, baseDir: File, streamz: TaskStreams): Unit = {
    val jar = new File(baseDir, "DynamoDBLocal.jar")
    PidUtils.extractPid("jps -ml".!!, port, jar) match {
      case Some(pid) =>
        streamz.log.info("Stopping dynamodb local")
        PidUtils.killPidCommand(pid).!
      case None =>
        streamz.log.warn("Cannot find dynamodb local PID")
    }
    if (clean) dbPathOpt.foreach { dbPath =>
      streamz.log.info("Cleaning dynamodb local")
      val dir = new File(dbPath)
      if (dir.exists()) sbt.IO.delete(dir)
    }
  }

}
