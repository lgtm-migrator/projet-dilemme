# DILemme
*A static site generator*
<br>

## Description
DILemme is a static site generator built in java using the command line. 
- It is a simple, fast, and easy to use. 
- It converts a structure of markdown files into a working static website. 
- You can even run the website directly from the tool. 
- And for the most advanced users, you can also create your own templates to customize the look and feel of the website.

---
## User manual
### Quick start example
#### 1. Initialize a website named *myAmazingWebsite*
```
java -jar dilemme.jar init myAmazingWebsite
```
The command will generate a basic site structure: <br>
```
myAmazingWebsite  => website folder (created in the current directory) 
├── config.json   => Global website config file (see section below)
└── index.md      => Site page (see section below)
```
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
The built site will be generated inside the `build` folder:<br>
```
myAmazingWebsite
├── build
│     └── index.html   => html page generated
├── config.json
└── index.md
```
#### 4. Serve the website (and keep it synchronized with changes)
After the website is built we can serve it (on a local http server) with the following command:  
(the `--watch` option will automatically rebuild the website when a file is changed)
```
java -jar dilemme.jar serve --watch myAmazingWebsite
```

#### 5. Enjoy
The site is now ready to be visited on your browser on the following address: [http://localhost:8080/](http://localhost:8080/)  
And now if you change the `index.md` file, or add new files, the site will be automatically rebuilt and updated.  
(You still need to refresh the page to see the changes)

<br><br>
### Commands

Every command follow the same syntax: `java -jar dilemme.jar <command> <site-path>`   
| Command         | Function                                                          | Requirements        |
|-----------------|-------------------------------------------------------------------|---------------------|
| `init`          | Create the base architecture and generate the template files      | None                |
| `build`         | Convert the template files to html into a `build` folder          | initialized website |
| `build --watch` | Same as build but after every file change everything is rebuilt   | initialized website |
| `clear`         | Delete the `build` folder                                         | built website       |
| `serve`         | Serve the built site on a local http server                       | built website       |
| `serve --watch` | Same as serve but the website keep synchronized with file changes | built website       |
| `-version`      | Display the version of the dilemme tool                           | None                |
<br><br>
### Config file
The config file is a json file that contains all the configuration of the website.
It contains the following fields:
- `title` : the title of the website
- `owner` : the name of the owner of the website
- `domain` : the domain of the website
<br><br>
### Site pages
Each site page (with a `.md` extention) is a site page. <br>
When you run the [init command](#Commands) `index.md` will be generated, you also can use it as a template to create other site page. <br>
The file is in two parts:
1. The page configuration, where you can configure multiple things:
    - `title`: the page title that will be displayed on the top of the page
    - `author`: the author of website
    - `date`: the date of the page creation (in the format yyyy-MM-dd)
2. The page content, that will be converted to a real HTML page afterward. You can write any content in the [Markdown format](https://www.markdownguide.org/cheat-sheet/).
This content can include links to other pages, images, videos, etc. Every media file will be copied to the `build` folder.

<br>

### Templates
A template is one or many "pseudo-html" files defining the frame of all your pages.  
For example, you can add a header, a footer, a sidebar, a menu, etc.  
To create a template you have to create a `template` folder at the root of your website and add a `layout.html` file inside.  
If you want to create an even more complex template you can add "sub layout" files inside the `template` folder that will be included in `layout.html`.

#### Real example of the using of a template:

- Structure of a site with a template:
  ```
  .
  ├── build/
  ├── config.json
  ├── index.md
  └── template/
         ├── layout.html
         └── menu.html

  ```
<br>


- The main layout file : `layout.html`:
  ```html
  <html lang="en">
  <head>
      <meta charset="utf-8">
      <title>{{ site.title }} | {{ page.title }}</title>
  </head>
  <body>
  {{> menu }}
  {{{ content }}}
  </body>
  <footer>
      <p>Page créée par {{ page.author }} sur le site de {{ site.owner }}</p>
  </footer>
  </html>
  ```
  <br>

  A sub layout file : `menu.html`:
  ```html
  <ul>
      <li><a href="index.html">home</a></li>
      <li><a href="https://dlang.org/">Awesome language</a></li>
  </ul>
  ```


  The files are in HTML but contain various variables (in `{}`):
  - `{{{ content }}}` : the place where the content of the page will be fulled  
  (Watchout the 3 `{}` !)
  - Sub layout files:
    - `{{> menu }}` : inserts the content of the sub layout file `menu.html`  
    (You can insert any sub layout writing its name without the extension)
  - Variables defined in `config.json`:
    - `{{ site.title }}` : the title of the website 
    - `{{ site.owner }}` : the name of the owner of the website

  - Variables defined in the header of the page:
    - `{{ page.title }}` : the title of the page (defined in the header of the page)
    - `{{ page.author }}` : the author of the page
    - `{{ page.date }}` : the date of the page creation
<br> <br>

### Limitations
This tool has a lot of great features, but it also has some limitations:
- You cannot create a folder named `build` inside other folders
- If you add a folder while you have the `--watch` option, the tool will not be able to find the folder