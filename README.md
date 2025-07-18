# server.markhome.msscf.msscf.cflib.dbtest
MSS Code Factory 3.0 CFLib Transactional JPA test framework application to demonstrate JPA configuration and basic structure

This test application was used primarily to figure out all the plumbing and glue required to set up a functional JPA environment with transactional annotations, custom pkey data types, nested transactions across multiple repositories, and generally provide a starting point framework for future JPA development.

Without a good foundation, you can't build a house, and it turns out that there is a dearth of complete and up-to-date examples that focus specifically on Spring JPA setup.  I also suspect there were some bugs in Spring that were fixed over the past two dot-releases since I started my efforts.

I started by using the Claude 3.5 free LLM under VSCode GitHub copilot to try to structure the code for Spring JPA, but that resulted in exceptions being thrown about conflicts between Spring code and the Hibernate layer configuration and code, so that was a failure.

Next I turned to Google's online summaries through the search engine to try to migrate the code to pure Jakarta JPA, and got that working to some degree, though, as it turns out, in a very non-standard fashion because I based it on my memory of a recent project's code structure which had a 20 year history and probably _was_ current-generation style code when first written, but is woefully out of sync with what I had in the end.

Finally a couple of Spring dot releases later, I turned to VSCode GitHub copilot again, but this time with OpenAI 4.0, and started using Query mode to carefully migrate my working Jakarta JPA code to Spring JPA code, while ensuring that Spring's auto-mapping functionality didn't usurp what I'd implemented in Jakarta JPA, which necessitated blocking a significant number of Spring initializers.

But I'm quite pleased with the result. This framework will let me implement data siloing for each of the JPA repositories in an application, and use as many JPA repositories as I feel a need for.  I might even try to _modularize_ the repositories in such a fashion that they can be "plugged" into the final executable much like a shared library or DLL can be hooked into C/C++ code through environment variables and path manipulation. But that would be a couple of years down the road, as it doesn't fit in with my current vision of "how to internet."
