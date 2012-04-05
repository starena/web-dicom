I-Logix-RPY-Archive version 8.0.9 Java 893629
{ IProject 
	- _id = GUID e975e2d4-7c0c-4ebd-b42f-45a8f164c21d;
	- _myState = 8192;
	- _name = "webdicom";
	- _lastID = 2;
	- _UserColors = { IRPYRawContainer 
		- size = 16;
		- value = 16777215; 16777215; 16777215; 16777215; 16777215; 16777215; 16777215; 16777215; 16777215; 16777215; 16777215; 16777215; 16777215; 16777215; 16777215; 16777215; 
	}
	- _defaultSubsystem = { ISubsystemHandle 
		- _m2Class = "ISubsystem";
		- _filename = "Default.sbs";
		- _subsystem = "";
		- _class = "";
		- _name = "Default";
		- _id = GUID 942f29b3-a5da-4a49-8776-2999c8907fcf;
	}
	- _component = { IHandle 
		- _m2Class = "IComponent";
		- _filename = "DefaultComponent.cmp";
		- _subsystem = "";
		- _class = "";
		- _name = "DefaultComponent";
		- _id = GUID 5169a5be-41ca-4c4b-b489-5b8c19676921;
	}
	- Multiplicities = { IRPYRawContainer 
		- size = 4;
		- value = 
		{ IMultiplicityItem 
			- _name = "1";
			- _count = 0;
		}
		{ IMultiplicityItem 
			- _name = "*";
			- _count = -1;
		}
		{ IMultiplicityItem 
			- _name = "0,1";
			- _count = -1;
		}
		{ IMultiplicityItem 
			- _name = "1..*";
			- _count = -1;
		}
	}
	- Subsystems = { IRPYRawContainer 
		- size = 3;
		- value = 
		{ ISubsystem 
			- fileName = "Default";
			- _id = GUID 942f29b3-a5da-4a49-8776-2999c8907fcf;
		}
		{ ISubsystem 
			- fileName = "webdicom";
			- _id = GUID 521d3a46-1337-4062-9390-ad4f82175e2c;
		}
		{ ISubsystem 
			- fileName = "java";
			- _id = GUID 64848a76-2ea1-4483-9656-7a57a3975c17;
		}
	}
	- Diagrams = { IRPYRawContainer 
		- size = 1;
		- value = 
		{ IDiagram 
			- fileName = "Model1";
			- _id = GUID 83205ad2-08f0-4a94-92f0-8df4fa564180;
		}
	}
	- Components = { IRPYRawContainer 
		- size = 1;
		- value = 
		{ IComponent 
			- fileName = "DefaultComponent";
			- _id = GUID 5169a5be-41ca-4c4b-b489-5b8c19676921;
		}
	}
}

