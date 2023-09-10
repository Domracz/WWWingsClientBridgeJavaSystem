//MCCScript 1.0

MCC.LoadBot(new ExampleChatBot());

//MCCScript Extensions

// The code and comments above are defining a "Script Metadata" section

// Every single chat bot (script) must be a class which extends the ChatBot class.
// Your class must be instantiates in the "Script Metadata" section and passed to MCC.LoadBot function.
class ExampleChatBot : ChatBot
{
    // This method will be called when the script has been initialized for the first time, it's called only once
    // Here you can initialize variables, eg. Dictionaries. etc...
	public override void Initialize()
	{
		LogToConsole("An example Chat Bot has been initialized!");
	}

    // This is a function that will be run when we get a chat message from a server
    // In this example it just detects the type of the message and prints it out
	public override void GetText(string text)
	{
		LogToConsole(text);
	}
}