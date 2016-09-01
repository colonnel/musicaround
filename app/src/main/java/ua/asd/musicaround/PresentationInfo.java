package ua.asd.musicaround;


public enum PresentationInfo {
    FirstScreen(R.string.citation_1),
    SecondScreen(R.string.citation_2),
    ThirdScreen(R.string.citation_3);

    final int citation;


    PresentationInfo(int citation) {
        this.citation = citation;
    }

    public int getCitation() {
        return citation;
    }
}





