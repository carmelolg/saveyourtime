package it.carmelolagamba.saveyourtime.service

import it.carmelolagamba.saveyourtime.SaveYourTimeApplication
import it.carmelolagamba.saveyourtime.persistence.App
import it.carmelolagamba.saveyourtime.persistence.DBFactory
import javax.inject.Inject

class AppService @Inject constructor() {

    fun findAllChecked(): List<App> {
        return DBFactory.getDatabase(SaveYourTimeApplication.context).applicationDao().getAllActive()
    }

    fun findByPackageName(packageName: String): App {
        return DBFactory.getDatabase(SaveYourTimeApplication.context).applicationDao().getByPackageName(packageName)
    }

/*    fun findIdByPackageName(packageName: String): Float {
        return DBFactory.getDatabase(SaveYourTimeApplication.context).applicationDao().getByPackageName(packageName).id
    }
*/
    fun insert(app: App) {
        DBFactory.getDatabase(SaveYourTimeApplication.context).applicationDao().insertAll(app)
    }

    fun upsert(app: App): App {
        val entity : App = this.findByPackageName(app.packageName!!)
        return if(entity != null) {
            DBFactory.getDatabase(SaveYourTimeApplication.context).applicationDao().update(app)
            app
        }else {
            this.insert(app)
            app
        }
    }

    fun resetAll() {
        var apps: List<App> = DBFactory.getDatabase(SaveYourTimeApplication.context).applicationDao().getAll()
        apps.forEach { app ->
            app.notifyTime = 60
            app.todayUsage = 0
            app.selected = false
            upsert(app)
        }
    }

    fun resetAllUsages() {
        var apps: List<App> = DBFactory.getDatabase(SaveYourTimeApplication.context).applicationDao().getAll()
        apps.forEach { app ->
            app.todayUsage = 0
            upsert(app)
        }
    }

    fun findNameByPackageName(packageName: String): String{
        return findByPackageName(packageName).name
    }

    fun appListPackageName(): List<String> {
        return findAllChecked().map { it.packageName }
    }
}