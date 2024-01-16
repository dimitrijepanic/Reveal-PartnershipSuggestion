
## Overview
Dear Reader,

I have implemented the Reveal Backend Engineer Project - PartnershipSuggestion in Java. Overall idea was to try to decouple the system services, while theoretically allowing for them to be expanded in the future, with adequate scallability patterns. The diagram below depicts the high level architecture of the system.
<p align="center" width="100%">
   <img width="33%" alt="overview" src="https://github.com/dimitrijepanic/Reveal-PartnershipSuggestion/assets/82520610/6d29f5b9-58d5-494e-8cb3-41e54fe34474">
</p>
Future sections will go into more detail about each of the domain logic components.
<\br>
Author: Dimitrije Panic

## Cache Service
## Suggestion Generating Service
## Timer Receiver Service
## Timer Start Service
## Suggestion Update Service
## Testing 
## Future Works
* Horizontal Scaling
## SQL or NoSQL Discussion

* Updates
  - NoSQL will allow for faster reads, however since the DB is completely denormalized updating one recommendation we will have to fetch the complete file
  - Updates (Writes) will occur more commonly than reads? 
* Sync Scrolling
  - While you type, LivePreview will automatically scroll to the current location you're editing.
* GitHub Flavored Markdown  
* Syntax highlighting
* [KaTeX](https://khan.github.io/KaTeX/) Support
* Dark/Light mode
* Toolbar for basic Markdown formatting
* Supports multiple cursors
* Save the Markdown preview as PDF
* Emoji support in preview :tada:
* App will keep alive in tray for quick usage
* Full screen mode
  - Write distraction free.
* Cross platform
  - Windows, macOS and Linux ready.

## How To Use

To clone and run this application, you'll need [Git](https://git-scm.com) and [Node.js](https://nodejs.org/en/download/) (which comes with [npm](http://npmjs.com)) installed on your computer. From your command line:

```bash
# Clone this repository
$ git clone https://github.com/amitmerchant1990/electron-markdownify

# Go into the repository
$ cd electron-markdownify

# Install dependencies
$ npm install

# Run the app
$ npm start
```

