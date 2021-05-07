package ast;

import java.util.HashMap;

/* We use a HashMap<String,Boolean> to track both declared variables (the keys of the map) and assigned variables (those for which the mapping maps to true)
   Note that a variable can't be assigned and not yet declared without failing our checks, so there's arguably no need to represent this case.
   Of course, you might think of a different design (especially in terms of how to represent the contextual information needed) */
/* a comment */ public class TinyVarsChecker implements /* in the middle of a line */tinyVarsVisitor<HashMap<String,Boolean>, String> {
// this comment may contain a /*
    /* and this comment may contain a // */
    /*here are back to back comments*//*see told ya*///i wasnt kidding
    /* how about his one
which continues onwards here*/}

