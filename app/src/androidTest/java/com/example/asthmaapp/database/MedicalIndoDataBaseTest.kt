package com.example.asthmaapp.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.asthmaapp.model.MedicamentInfo
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import com.google.common.truth.Truth.assertThat as assertThat1


//RunWith ннотации RunWith указывается так называемый Runner,
// который и отвечает за запуск тестов, корректный вызов и обработку всех методов.
//@RunWith(AndroidJUnit4::class) // Annotate with @RunWith
//class MedicalIndoDataBaseTest : TestCase() {
//    // get reference to the LanguageDatabase and LanguageDao class
//    private lateinit var db: MedicalIndoDataBase
//    private lateinit var dao: MedicalInfoDao
//
//    // Override function setUp() and annotate it with @Before
//    // this function will be called at first when this test class is called
//    @Before
//    //Код метода с аннотацией Before будет выполняться перед выполнением каждого тестового метода.
//    public override fun setUp() {
//        // get context -- since this is an instrumental test it requires
//        // context from the running application
//        val context = ApplicationProvider.getApplicationContext<Context>()
//        // initialize the db and dao variable
//        db = Room.inMemoryDatabaseBuilder(context, MedicalIndoDataBase::class.java).build()
//        dao = db.medicalInfoDao()
//    }
//
//    // Override function closeDb() and annotate it with @After
//    // this function will be called at last when this test class is called
//    //After будет выполняться после каждого тестового метода
//    @After
//    fun closeDb() {
//        db.close()
//    }
//
//    // create a test function and annotate it with @Test
//    // here we are first adding an item to the db and then checking if that item
//    // is present in the db -- if the item is present then our test cases pass
//    @Test
//    fun writeAndReadLanguage() = runBlocking {
//        val medicament = MedicamentInfo(0, "Pillow", 222, 2)
//        dao.addMedicalInfo(medicament)
//        val medicaments = dao.readAllData()
//        var medicationItem = medicaments.value
//        if (medicationItem != null) {
//// "assert" методы проверяют, соответствует ли результат работы
//            assertThat1(medicationItem.contains(medicament)).isTrue()
//        }
//    }
//}
