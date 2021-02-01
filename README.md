# BotCovidLab
**BotCovidLab** is an application, that powers chat-bots on several social network platforms. It serves 2 main purposes:
- Provides users with latest [COVID-19 stats](#covid-19-data)
- Makes procedure of applying for a COVID test easier. Data of patients who apply for COVID-19 tests are stored on Firebase Cloud Firestore database.

# Prerequisites
*Here will be technical information about tools used and how to use the application*
SpringBoot framework, Maven, Java, Firebase

# Functionality
### Telegram - "LvKoronaTrc"
To start interaction with the chat-bot press ***"start"*** or type "***/start***" in the chat window.
**Getting COVID-19 stats**
- "Get Covid Stats For Latvia" - provides latest statistics about infected, recovered and deceased patients in Latvia.
- "Get Worldwide Covid-19 statistics" - provides latest statistics about infected, recovered and deceased patients globaly.

**Applying for COVID-19 test**
Choose option *Apply for Covid-19 test in Latvia*. Provide chatbot with the required information:
- your name and surname
- personal code
- symptoms and temperature
- if you have been in contact with a Covid-19 patient
- your phone number.

If bot will consider provided information invalid, it will ask to provide correct information. (E.g. name should be at least 3 letters long; phone should start with "*2*" or "* 6*", etc.)
Data will be saved in a database and employee of the lab will contact you to agree on the time and date of the test.

### Facebook - "Covid-19 Chat Bot"
To start interaction with the chat-bot, type anything in the chat window. (E.g. be nice and say "*hello*" to the chat-bot).
Further interaction happens by pressing buttons accordingly to needed information.
**Getting COVID-19 stats**
- "Covid Latvia" - provides latest statistics about infected, recovered and deceased patients in Latvia.
- "Covid worldwide" - provides latest statistics about infected, recovered and deceased patients globaly.
- "Covid by country" - provides latest statistics about infected, recovered and deceased patients about the country of your choice. To choose the country, type it's name in the chat window. Use country names in English (e.g. "***Lithuania***")
**Applying for COVID-19 test**
Choose option *Apply for Covid-19 test in Latvia*. Provide chatbot with the required information:
- your name and surname
- personal code
- symptoms and temperature
- if you have been in contact with a Covid-19 patient
- your phone number.

If bot will consider provided information invalid, it will ask to provide correct information. (E.g. name should be at least 3 letters long; phone should start with "*2*" or "* 6*", etc.)
Data will be saved in a database and employee of the lab will contact you to agree on the time and date of the test.

### WebAplication for database management
here be information about how to use webaplication.

# Covid-19 Data
COVID-19 data is being provided by [**Disease.sh - Open Disease data**](https://corona.lmao.ninja/)

# Future plans/potential for development
- creating chat-bots on other social network platforms (Twitter, WhatsApp, Discord, Slack, etc.)
- improving application process for tests by adding option to reserve time and date directly through the chat-bot.
