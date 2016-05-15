
#ifndef AIRFIELD
#define AIRFIELD

//#include <Ice/BuiltinSequences.ice> 

module Airfield
{
	exception RequestCanceledException
	{
	};
	
			
	class AirportInfo
	{
		string code;		
		int load;		
		int getLoad(string code);
		void addLoad(string code, int load);
	};	
};

#endif
