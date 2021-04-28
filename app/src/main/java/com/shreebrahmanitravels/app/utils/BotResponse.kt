package com.shreebrahmanitravels.app.utils
import com.shreebrahmanitravels.app.utils.Constants.OPEN_GOOGLE
import com.shreebrahmanitravels.app.utils.Constants.OPEN_SEARCH
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat
object BotResponse {
    fun basicResponses(_message: String): String {

        val random = (0..2).random()
        val message =_message.toLowerCase()

        return when {
            //Flips a coin
            message.contains("flip") && message.contains("coin") -> {
                val r = (0..1).random()
                val result = if (r == 0) "heads" else "tails"

                "I flipped a coin and it landed on $result"
            }

            //Math calculations
            message.contains("solve") -> {
                val equation: String? = message.substringAfterLast("solve")
                return try {
                    val answer = SolveMath.solveMath(equation ?: "0")
                    "$answer"

                } catch (e: Exception) {
                    "Sorry, I can't solve that."
                }
            }

            //Hello
            message.contains("hello") -> {
                when (random) {
                    0 -> "Hello there!, how may i help you?"
                    1 -> "Buongiorno!"
                    else -> "error" }
            }

            //How are you?
            message.contains("how are you") -> {
                when (random) {
                    0 -> "I'm doing fine, thanks!"
                    1 -> "I'm hungry..."
                    2 -> "Pretty good! How about you?"
                    else -> "error"
                }
            }


            message.contains("1 bus service") -> {
                when (random) {
                    0 -> "What is bus booking? => here, Bus booking provides you to choose your university Up-Down transportation through your internet" +
                            "\nWhen bus will board ? => at ... a.m and reach your station as it said to you..." +
                            "\nWhy is bus Bokking? => To provide you hassle free seating booking on your finger tip and connecting your journey with technology on Internet." +
                            "\nOur services \n" +
                            "Hassle-free Ride booking service\n" +
                            "Add/remove own vehicles to the list\n" +
                            "Verification of students by workplace mail.\n" +
                            "Reduces trust issue.\n" +
                            "Cashless transactions\n" +
                            "Digital database for students details\n" +
                            "Chat support by management.\n" +
                            "Realtime notification\n"
                    else -> "error"
                }
            }

            message.contains("2 agency") -> {
                when (random) {
                    0 -> "To know about Ahmedabad to University enter \"ATU\" and for Vadodara to University enter \"VTU\"."
                    else -> "error"
                }
            }
            message.contains("ATU") -> {
                when (random) {
                    0 -> "Ahmedabad to University : Here we have 2 register Agency 1)Bhramani Travel 2.)Ajitnath Travel." +
                            "\nTo know about their SERVICES AND FEES STRUCTURE you can MAKE CALL to their office or visit their website." +
                            "\n Bhramani travels ==> 9876543210 ,  Ajitnath Travels ==> 0123456789"
                    else -> "error"
                }
            }

            message.contains("VTU") -> {
                when (random) {
                    0 -> "Vadodara to University : Here we have 2 register Agency 1)Sunita Travel 2.)XYZ Travel." +
                            "\nTo know about their SERVICES AND FEES STRUCTURE you can MAKE CALL to their office or visit their website." +
                            "\n Sunita travels ==> 9876543210 ,  XYZ Travels ==> 0123456789"
                    else -> "error"
                }
            }

            message.contains("3 developer") -> {
                when (random) {
                    0 -> "Nishit Patel and Breeze Patel" +
                            "\n further details we will upload here soon"
                    else -> "error"
                }
            }

            message.contains("4 contact us") -> {
                when (random) {
                    0 -> "There are 3 ways to can contact us. they are as follow" +
                            "\n1.)By this chatbot, 2.Call support, 3.)Email us:- Rideshar@gmail.com" +
                            "\nDo note this will make to contact to Rideshare Application services provider" +
                            "\nIf you want to contact the particular agency you can call them to their agency, and for that detail enter \"2 agency\". " +
                            "\nfor any kind to fraud or any technical problem feel free to contact us via Email we are here for you <3."
                    else -> "error"
                }
            }

            message.contains("5 rideshare") -> {
                when (random) {
                    0 -> "It provide online Ride Sharing service.\n" +
                            "RIDESHARE connects car owners and co-travellers to share city-to-University journeys, so that they can share a long distance trip together, and both save money." +
                            "\nYou can share your vehicle with same organisation   "
                    else -> "error"
                }
            }
            message.contains("6 fee payment") -> {
                when (random) {
                    0 -> "For fee payment you can freely pay with out PAYMENT GATEWAY via your Debit/Credit Card." +
                            "\nAnd for RideShare UPI transaction is also we provided."
                    else -> "error"
                }
            }
            //What time is it?
            message.contains("time") && message.contains("?")-> {
                val timeStamp = Timestamp(System.currentTimeMillis())
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
                val date = sdf.format(Date(timeStamp.time))

                date.toString()
            }

            //Open Google
            message.contains("open") && message.contains("google")-> {
                OPEN_GOOGLE
            }

            //Search on the internet
            message.contains("search")-> {
                OPEN_SEARCH
            }

            //When the programme doesn't understand...
            else -> {
                when (random) {
                    0 -> "I don't understand..."
                    1 -> "Try asking me something different"
                    else -> "error"
                }
            }
        }
    }
}