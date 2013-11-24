package projekt.pogodynkatab;


public class PogodaZListy
{
    public String name;
    public String resourceId;
    public String folder;

    public PogodaZListy()
        {
            // TODO Auto-generated constructor stub
        }

  //  public OkresowaPogoda(String name, String abbreviation, String region, String resourceFilePath)
    public PogodaZListy(String name, String resourceFilePath, String folder)
        {
            this.name = name;
            this.resourceId = resourceFilePath;
            this.folder = folder;
         
        }

    @Override
    public String toString()
        {
            return this.name;
        }
}