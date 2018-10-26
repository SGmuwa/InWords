﻿namespace InWords.WebApi
{
    using Microsoft.AspNetCore.Hosting;
    using Microsoft.AspNetCore.Mvc;
    using Microsoft.Extensions.Configuration;
    using Microsoft.Extensions.DependencyInjection;
    using Microsoft.AspNetCore.Builder;
    using Microsoft.AspNetCore.Authentication.JwtBearer;
    using InWords.Auth;
    using InWords.Data;

    public class Startup
    {
        public Startup(IHostingEnvironment env)
        {
            var builder = new ConfigurationBuilder()
                .SetBasePath(env.ContentRootPath)
                .AddJsonFile("appsettings.json", optional: false, reloadOnChange: true)
                .AddJsonFile($"appsettings{env.EnvironmentName}.json", optional: true)
                .AddJsonFile("appsettings.security.json", optional: false, reloadOnChange: true)
                .AddEnvironmentVariables();
            Configuration = builder.Build();
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
                .AddJwtBearer(AuthOptions.TokenProvider.ValidateOptions);
            services.AddMvc().SetCompatibilityVersion(CompatibilityVersion.Version_2_1);

            // 'scoped' in ASP.NET means "per HTTP request"
            services.AddScoped<InWordsDataContext>(
                _ => new InWordsDataContext(Configuration.GetConnectionString("DefaultConnection")));
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IHostingEnvironment env)
        {
            //if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }

            app.UseAuthentication();
            app.UseMvc();
        }
    }
}


///feature — используется при добавлении новой функциональности уровня приложения
///fix — если исправили какую-то серьезную багу
///docs — всё, что касается документации
///style — исправляем опечатки, исправляем форматирование
///refactor — рефакторинг кода приложения
///test — всё, что связано с тестированием
///chore — обычное обслуживание кода
