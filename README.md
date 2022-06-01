# DILemme
> A static site generator

## Description
TODO

---
## User manual
### Quick start example
#### 1. Initialize a website named *myAmazingWebsite*
```
java -jar dilemme.jar init myAmazingWebsite
```
The command will generate a basic site structure: <br>
`myAmazingWebsite` : website folder (created in the current directory)<br> 
`├── config.json` : Global website config file ([more info](#config-file))<br>
`└── index.md` : Site page ([more info](#site-pages))<br> 

#### 2. Create a page
Add content to the `index.md` file (in [Markdown](https://www.markdownguide.org/cheat-sheet/)):
```
{
  "title": "Main page",
  "author": "Eliott Chytil",
  "date": "2022-05-25"
}
---
# Welcome to my amazing website
Wow this is truely amazing !
```

#### 3. Build the website
Now the page is created we can build the website with the following command:
```
java -jar dilemme.jar build myAmazingWebsite
```
The built site will be generated inside the build folder:<br>
`myAmazingWebsite`<br>
`├── build`<br>
`│`&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`└── index.html` : html page generated<br>
`├── config.json`<br>
`└── index.md`<br>

#### 4. Serve the website
After the website is built we can serve it (on a local http server) with the following command:
```
java -jar dilemme.jar serve myAmazingWebsite
```

#### 5. Enjoy
The site is now ready to be visited on your browser on the following address: [http://localhost:8080/](http://localhost:8080/)

### Commands
| Command    | Function                                                     | Requirements        |
|------------|--------------------------------------------------------------|---------------------|
| `init`     | Create the base architecture and generate the template files | None                |
| `build`    | Convert the template files to html into a `build` folder     | initialized website |
| `clear`    | Delete the `build` folder                                    | built website       |
| `serve`    | Serve the built site on a local http server                  | built website       |
| `-version` | Display the version of the dilemme tool                      | None                |

### Config file
The config file is a json file that contains all the configuration of the website.
It contains the following fields:
- `title` : the title of the website
- `owner` : the name of the owner of the website
- `domain` : the domain of the website

### Site pages
Each site page (with a `.md` extention) is a site page. <br>
When you run the [init command](#init) `index.md` will be generated, you also can use it as a template to create other site page. <br>
The file is in two parts:
1. The page configuration, where you can configure mutiple things:
    - `title`: the page title that will be displayed on the top of the page
    - `author`: the author of website
    - `date`: the date of the page creation (in the format yyyy-MM-dd)
2. The page content, that will be converted to a real HTML page afterward. You can write any content in the [Markdown format](https://www.markdownguide.org/cheat-sheet/).
This content can include links to other pages, images, videos, etc. Every media file will be copied to the `build` folder.