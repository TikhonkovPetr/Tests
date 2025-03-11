fun main()
{
    val library:Library = Library()
    val userMenu:UserMenu = UserMenu()
    while(true) {
        try {
            val choiceList:Int = userMenu.MainMenu()
            library.ShowAnyList(choiceList)
            val choiceId:Int = userMenu.ChoiceListMenu()
            library.ShowSerchItem(choiceList,choiceId)
            var choiseActivity:Int = userMenu.ChoiceActivityWithObject()
            library.Activity(choiceList,choiseActivity,choiceId)
        }
        catch (ex:ArrayStoreException){
            println(ex.message)
            continue
        }
        catch (ex:Exception){
            println(ex.message)
            break
        }
    }
}

class Library():DataLibrary(){
    private fun ShowBook(){
        println("Список книг:")
        ShowList(listBook)
    }
    private fun ShowNewspaper(){
        println("Список газет:")
        ShowList(listNewspaper)
    }
    private fun ShowDisk(){
        println("Список дисков:")
        ShowList(listDisk)
    }
    fun ShowAnyList(choiceUser: Int){
        when(choiceUser){
            1 -> ShowBook()
            2 -> ShowNewspaper()
            3 -> ShowDisk()
            4 -> throw Exception("Выход из программы")
            else -> {
                throw Exception("Был введён не корректный запрос")
            }
        }
    }
    fun ShowSerchItem(listNumber: Int,searchID: Int){
        when(listNumber){
            1 -> (getSerchItem(listBook,searchID) as Book).getFullInfo()
            2 -> (getSerchItem(listNewspaper,searchID) as Newspaper).getFullInfo()
            3 -> (getSerchItem(listDisk,searchID) as Disk).getFullInfo()
            else ->{
                throw Exception("Был введён не корректный запрос")
            }
        }
    }

    fun Activity(listNumber: Int,choiceUserActivity: Int,searchID: Int){
        when(listNumber){
            1 ->RealisActivity(listBook,searchID,choiceUserActivity)
            2 ->RealisActivity(listNewspaper,searchID,choiceUserActivity)
            3 ->RealisActivity(listDisk,searchID,choiceUserActivity)
        }
    }
    fun <T> getSerchItem(selectedList:List<T>,searchID:Int):T{
        val serchObject = SearchObjectID(selectedList,searchID)
        if(serchObject==null){
            throw Exception("Нет объекта с таким Id")
        }
        return serchObject
    }

}

class Book(ID: Int, Name: String, Available: Boolean,val countPages:Int,val autor:String) :ObjectLibrary(ID, Name, Available),FullInfoInterface,ReadingInLibrary,TakeToHome{
    override fun getFullInfo() {
        println("книга: ${this.Name} (${this.countPages} стр.) автора: ${this.autor} с id: ${this.ID} доступна:${YesOrNot(Available)}")
    }

    override fun readingInLibrary() {
        if(Available){
            println("Вы взяли книгу ${this.Name} читать в зале")
            Available=!Available
        }
        else println("Эту книгу уже кто то взял")
    }

    override fun takeToHome() {
        if(Available){
            println("Вы взяли книгу домой ${this.Name}")
            Available=!Available
        }
        else println("Эту книгу уже кто то взял")
    }
}
class Newspaper(ID: Int, Name: String, Available: Boolean, val issueNumber:Int) :ObjectLibrary(ID, Name, Available),FullInfoInterface,ReadingInLibrary{
    override fun getFullInfo() {
        println("выпуск: ${this.issueNumber} газеты ${this.Name} с id: ${this.ID} доступен: ${YesOrNot(Available)}")
    }
    override fun readingInLibrary() {
        if(Available){
            println("Вы взяли газету ${this.Name} читать в зале")
            Available=!Available
        }
        else println("Эту газету уже кто то взял читать")
    }
}
class Disk(ID: Int, Name: String, Available: Boolean,val typeDiscID:Int) :ObjectLibrary(ID, Name, Available),FullInfoInterface,TakeToHome{
    val typeDisc:Map<Int,String> = mapOf(0 to "DVD", 1 to "CD")
    override fun getFullInfo() {
        println("${this.typeDisc.get(typeDiscID)} ${this.Name} доступен: ${YesOrNot(Available)}")
    }
    override fun takeToHome() {
        if(Available){
            println("Вы взяли домой диск ${this.Name}")
            Available = !Available
        }
        else println("Диска нет в библиотеке")
    }
}

class UserMenu(){
    fun MainMenu():Int {
        println("Выберите действие:\n1. Показать книги\n2. Показать газеты\n3. Показать диски\n4. Завершить работу")
        return readln().toInt()
    }
    fun ChoiceListMenu(): Int{
        println("Выберите объект из списка по ID")
        return readln().toInt()
    }
    fun ChoiceActivityWithObject():Int{
        println("Выберите действие:\n1. Взять домой\n2. Читать в читальном зале\n3. Показать подробную информацию\n4. Вернуть\n5. вернуться на главную страницу")
        return readln().toInt()
    }
}

abstract class ObjectLibrary(val ID:Int,val Name:String,var Available:Boolean){
    fun BriefInformation(){
        println("ID: ${ID}| ${this.Name} доступна: ${YesOrNot(Available)}")
    }
    fun ReturnObject(){
        if(!Available){
            println("Объект ${this.Name} с ID: ${this.ID} вернули")
            Available = !Available
        }
        else{
            println("Этот объект находится в библиотеке!")
        }
    }
}
abstract class  DataLibrary(){
    val listBook:List<Book> = listOf(Book(1,"Ночь перед рождеством",true,96,"Николай Гоголь"),
        Book(12,"Мёртвые души",true,352,"Николай Гоголь"),
        Book(5,"Анна Каренина",false,864,"Лев Толстой"),
        Book(52,"Обломов", true,544,"Иван Гончаров")
    )
    val listNewspaper:List<Newspaper> = listOf(Newspaper(4,"Комсомольская правда",true,1235),
        Newspaper(8,"Известия",false,121),
        Newspaper(7,"РБК (газета)",true,96)
    )
    val listDisk:List<Disk> = listOf(Disk(2,"Пираты Карибского моря: проклятие Чёрной жемчужины",true,0),
        Disk(3,"Расомаха",false,1),
        Disk(6,"Астрал",false,1)
    )
}

fun YesOrNot(Available: Boolean):String{
    return (if(Available) "Да" else "Нет")
}
fun <T> ShowList(list:List<T>){
    list.forEach { element ->
        if(element is ObjectLibrary)(element as ObjectLibrary).BriefInformation()
    }
}
fun <T> SearchObjectID(list:List<T>,takeID: Int):T?{

    val serchObject:T? = list.firstOrNull{element ->(element as ObjectLibrary).ID==takeID}
    return serchObject
}
fun <T> RealisActivity(list:List<T>,searchID: Int,choiceUserActivity: Int){
    val library:Library =Library()
    val elem:T = library.getSerchItem(list,searchID)
    when(choiceUserActivity){
        1 -> {
            if(elem is TakeToHome){
                list.map { element ->
                    if(searchID == (element as ObjectLibrary).ID){
                        (element as TakeToHome).takeToHome()
                    }
                }
            }
            else println("Данный объект нельзя забрать домой")
        }
        2 ->{
            if(elem is ReadingInLibrary){
                list.map { element ->
                    if(searchID == (element as ObjectLibrary).ID){
                        (element as ReadingInLibrary).readingInLibrary()
                    }
                }
            }
            else println("Данный объект нельзя читать в библиотеке")
        }
        3 ->{
            if(elem is Book)(elem as Book).getFullInfo()
            else if(elem is Newspaper)(elem as Newspaper).getFullInfo()
            else if(elem is Disk)(elem as Disk).getFullInfo()
        }
        4 ->{
            list.map{ element ->
                if(searchID == (element as ObjectLibrary).ID){
                    element.ReturnObject()
                }
            }
        }
        5->{
            throw ArrayStoreException("Выход в главное меню")
        }
        else -> throw Exception("Вы выбрали несуществующи вариант")
    }
}

interface FullInfoInterface{
    fun getFullInfo()
}
interface TakeToHome{
    fun takeToHome()
}
interface ReadingInLibrary{
    fun readingInLibrary()
}